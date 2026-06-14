package com.vluk4.itunescodechallenge.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.vluk4.itunescodechallenge.core.data.mapper.toEntityOrNull
import com.vluk4.itunescodechallenge.core.database.ItunesDatabase
import com.vluk4.itunescodechallenge.core.database.entity.SongEntity
import com.vluk4.itunescodechallenge.core.network.ItunesNetworkDataSource
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
internal class SongRemoteMediator(
    private val query: String,
    private val database: ItunesDatabase,
    private val networkDataSource: ItunesNetworkDataSource,
) : RemoteMediator<Int, SongEntity>() {

    private val songDao = database.songDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SongEntity>,
    ): MediatorResult {
        if (loadType != LoadType.REFRESH) {
            return MediatorResult.Success(endOfPaginationReached = true)
        }

        return try {
            val results = networkDataSource.searchSongs(query, MAX_RESULTS)
            val entities = results.mapIndexedNotNull { index, dto ->
                dto.toEntityOrNull(searchQuery = query, sortIndex = index)
            }

            database.withTransaction {
                songDao.clearByQuery(query)
                songDao.clearInactiveSearchCaches(query)
                songDao.cacheSongs(entities)
            }

            MediatorResult.Success(endOfPaginationReached = true)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    companion object {
        /** Single-fetch size; the iTunes Search API caps `limit` at 200. */
        const val MAX_RESULTS = 200
    }
}
