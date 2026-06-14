package com.vluk4.itunescodechallenge.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.vluk4.itunescodechallenge.core.common.dispatcher.DispatcherProvider
import com.vluk4.itunescodechallenge.core.common.result.Outcome
import com.vluk4.itunescodechallenge.core.common.result.runCatchingOutcome
import com.vluk4.itunescodechallenge.core.data.mapper.toDomain
import com.vluk4.itunescodechallenge.core.data.mapper.toDomainOrNull
import com.vluk4.itunescodechallenge.core.data.mapper.toEntity
import com.vluk4.itunescodechallenge.core.data.paging.SongRemoteMediator
import com.vluk4.itunescodechallenge.core.database.ItunesDatabase
import com.vluk4.itunescodechallenge.core.database.dao.SongDao
import com.vluk4.itunescodechallenge.core.domain.model.Album
import com.vluk4.itunescodechallenge.core.domain.model.Song
import com.vluk4.itunescodechallenge.core.domain.repository.SongRepository
import com.vluk4.itunescodechallenge.core.network.ItunesNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class SongRepositoryImpl @Inject constructor(
    private val database: ItunesDatabase,
    private val songDao: SongDao,
    private val networkDataSource: ItunesNetworkDataSource,
    private val dispatcherProvider: DispatcherProvider,
) : SongRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun searchSongs(query: String): Flow<PagingData<Song>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            initialLoadSize = PAGE_SIZE,
            enablePlaceholders = false,
        ),
        remoteMediator = SongRemoteMediator(query, database, networkDataSource),
        pagingSourceFactory = { songDao.pagingSource(query) },
    ).flow.map { pagingData -> pagingData.map { it.toDomain() } }

    override fun observeRecentlyPlayed(): Flow<List<Song>> =
        songDao.observeRecentlyPlayed(RECENTLY_PLAYED_LIMIT)
            .map { entities -> entities.map { it.toDomain() } }

    override suspend fun getSong(id: Long): Song? = withContext(dispatcherProvider.io) {
        songDao.getById(id)?.toDomain()
    }

    override suspend fun markAsPlayed(song: Song) = withContext(dispatcherProvider.io) {
        songDao.cacheSongs(listOf(song.toEntity(lastPlayedAt = System.currentTimeMillis())))
    }

    override suspend fun getAlbum(collectionId: Long): Outcome<Album> =
        withContext(dispatcherProvider.io) {
            runCatchingOutcome {
                val results = networkDataSource.lookupAlbum(collectionId)
                val tracks = results.mapNotNull { it.toDomainOrNull() }
                songDao.cacheSongs(tracks.map { it.toEntity(lastPlayedAt = null) })
                val header = results.firstOrNull()
                Album(
                    id = collectionId,
                    name = header?.collectionName.orEmpty(),
                    artistName = header?.artistName.orEmpty(),
                    artworkUrl = tracks.firstOrNull()?.artworkUrl.orEmpty(),
                    genre = header?.primaryGenreName,
                    releaseDate = header?.releaseDate,
                    trackCount = header?.trackCount ?: tracks.size,
                    tracks = tracks,
                )
            }
        }

    private companion object {
        const val RECENTLY_PLAYED_LIMIT = 5
        const val PAGE_SIZE = 20
    }
}
