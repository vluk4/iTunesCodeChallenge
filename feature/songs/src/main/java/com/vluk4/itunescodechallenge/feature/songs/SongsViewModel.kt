package com.vluk4.itunescodechallenge.feature.songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vluk4.itunescodechallenge.core.domain.model.Song
import com.vluk4.itunescodechallenge.core.domain.usecase.GetRecentlyPlayedSongsUseCase
import com.vluk4.itunescodechallenge.core.domain.usecase.SearchSongsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SongsViewModel @Inject constructor(
    private val searchSongs: SearchSongsUseCase,
    getRecentlyPlayed: GetRecentlyPlayedSongsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SongsUiState())
    val uiState: StateFlow<SongsUiState> = _uiState.asStateFlow()

    /** Recently-played songs for the home header — cache-backed, offline-safe. */
    val recentlyPlayed: StateFlow<List<Song>> = getRecentlyPlayed()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), emptyList())

    /** Debounced, paginated search results driven by query inside the shared state. */
    val songs: Flow<PagingData<Song>> = _uiState
        .map { it.query }
        .debounce(SEARCH_DEBOUNCE_MS)
        .filter { it.isNotBlank() }
        .distinctUntilChanged()
        .flatMapLatest { searchSongs(it) }
        .cachedIn(viewModelScope)

    fun onQueryChange(value: String) {
        _uiState.update { it.copy(query = value) }
    }

    fun onSongSelected(song: Song) {
        _uiState.update { it.copy(selectedSong = song, isMoreOptionsVisible = true) }
    }

    fun onMoreOptionsDismissed() {
        _uiState.update { it.copy(selectedSong = null, isMoreOptionsVisible = false) }
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 350L
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
