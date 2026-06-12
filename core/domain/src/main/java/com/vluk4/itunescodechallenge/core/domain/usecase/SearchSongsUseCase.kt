package com.vluk4.itunescodechallenge.core.domain.usecase

import androidx.paging.PagingData
import com.vluk4.itunescodechallenge.core.domain.model.Song
import com.vluk4.itunescodechallenge.core.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchSongsUseCase @Inject constructor(
    private val repository: SongRepository,
) {
    operator fun invoke(query: String): Flow<PagingData<Song>> =
        repository.searchSongs(query.trim())
}
