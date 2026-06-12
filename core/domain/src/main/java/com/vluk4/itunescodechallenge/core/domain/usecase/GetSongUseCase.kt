package com.vluk4.itunescodechallenge.core.domain.usecase

import com.vluk4.itunescodechallenge.core.domain.model.Song
import com.vluk4.itunescodechallenge.core.domain.repository.SongRepository
import javax.inject.Inject

class GetSongUseCase @Inject constructor(
    private val repository: SongRepository,
) {
    suspend operator fun invoke(id: Long): Song? = repository.getSong(id)
}
