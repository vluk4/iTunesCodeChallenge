package com.vluk4.itunescodechallenge.feature.album

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vluk4.itunescodechallenge.core.common.result.Outcome
import com.vluk4.itunescodechallenge.core.domain.usecase.GetAlbumUseCase
import com.vluk4.itunescodechallenge.feature.album.navigation.ALBUM_ARG_COLLECTION_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val getAlbum: GetAlbumUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val collectionId: Long =
        savedStateHandle.get<Long>(ALBUM_ARG_COLLECTION_ID) ?: 0L

    private val _uiState = MutableStateFlow<AlbumUiState>(AlbumUiState.Loading)
    val uiState: StateFlow<AlbumUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = AlbumUiState.Loading
            _uiState.value = when (val outcome = getAlbum(collectionId)) {
                is Outcome.Success -> AlbumUiState.Success(outcome.data)
                is Outcome.Failure -> AlbumUiState.Error(outcome.error.message)
            }
        }
    }
}
