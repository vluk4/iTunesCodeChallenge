package com.vluk4.itunescodechallenge.core.domain.repository

import androidx.paging.PagingData
import com.vluk4.itunescodechallenge.core.common.result.Outcome
import com.vluk4.itunescodechallenge.core.domain.model.Album
import com.vluk4.itunescodechallenge.core.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {

    fun searchSongs(query: String): Flow<PagingData<Song>>

    fun observeRecentlyPlayed(): Flow<List<Song>>

    suspend fun getSong(id: Long): Song?

    suspend fun markAsPlayed(song: Song)

    suspend fun getAlbum(collectionId: Long): Outcome<Album>
}
