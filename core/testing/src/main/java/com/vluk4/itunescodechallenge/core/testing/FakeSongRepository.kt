package com.vluk4.itunescodechallenge.core.testing

import androidx.paging.PagingData
import com.vluk4.itunescodechallenge.core.common.result.Outcome
import com.vluk4.itunescodechallenge.core.domain.model.Album
import com.vluk4.itunescodechallenge.core.domain.model.Song
import com.vluk4.itunescodechallenge.core.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeSongRepository : SongRepository {

    var searchResults: List<Song> = emptyList()
    var albumResult: Outcome<Album> = Outcome.Failure(IllegalStateException("not set"))
    private val recentlyPlayed = MutableStateFlow<List<Song>>(emptyList())

    override fun searchSongs(query: String): Flow<PagingData<Song>> =
        flowOf(PagingData.from(searchResults))

    override fun observeRecentlyPlayed(): Flow<List<Song>> = recentlyPlayed

    override suspend fun getSong(id: Long): Song? =
        (searchResults + recentlyPlayed.value).firstOrNull { it.id == id }

    override suspend fun markAsPlayed(song: Song) {
        recentlyPlayed.value = (listOf(song) + recentlyPlayed.value).distinctBy { it.id }
    }

    override suspend fun getAlbum(collectionId: Long): Outcome<Album> = albumResult
}
