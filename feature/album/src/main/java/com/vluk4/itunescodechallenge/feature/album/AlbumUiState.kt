package com.vluk4.itunescodechallenge.feature.album

import com.vluk4.itunescodechallenge.core.domain.model.Album

sealed interface AlbumUiState {
    data object Loading : AlbumUiState
    data class Error(val message: String?) : AlbumUiState
    data class Success(val album: Album) : AlbumUiState
}
