package com.vluk4.itunescodechallenge.core.domain.usecase

import com.vluk4.itunescodechallenge.core.domain.model.Song
import com.vluk4.itunescodechallenge.core.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentlyPlayedSongsUseCase @Inject constructor(
    private val repository: SongRepository,
) {
    operator fun invoke(): Flow<List<Song>> = repository.observeRecentlyPlayed()
}
