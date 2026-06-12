package com.vluk4.itunescodechallenge.core.domain.usecase

import com.vluk4.itunescodechallenge.core.common.result.Outcome
import com.vluk4.itunescodechallenge.core.domain.model.Album
import com.vluk4.itunescodechallenge.core.domain.repository.SongRepository
import javax.inject.Inject

class GetAlbumUseCase @Inject constructor(
    private val repository: SongRepository,
) {
    suspend operator fun invoke(collectionId: Long): Outcome<Album> =
        repository.getAlbum(collectionId)
}
