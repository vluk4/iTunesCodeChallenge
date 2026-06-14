package com.vluk4.itunescodechallenge.feature.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vluk4.itunescodechallenge.core.domain.player.AudioPlayer
import com.vluk4.itunescodechallenge.core.domain.usecase.GetSongUseCase
import com.vluk4.itunescodechallenge.core.domain.usecase.MarkSongAsPlayedUseCase
import com.vluk4.itunescodechallenge.feature.player.navigation.PLAYER_ARG_SONG_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val getSong: GetSongUseCase,
    private val markAsPlayed: MarkSongAsPlayedUseCase,
    private val audioPlayer: AudioPlayer,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val songId: Long = savedStateHandle.get<Long>(PLAYER_ARG_SONG_ID) ?: 0L
    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    init {
        loadSong()
        observePlayback()
    }

    private fun loadSong() {
        viewModelScope.launch {
            val song = getSong(songId)
            if (song == null) {
                _uiState.update { it.copy(isLoading = false, error = PlayerError.SONG_UNAVAILABLE) }
                return@launch
            }
            markAsPlayed(song)
            _uiState.update { it.copy(isLoading = false, song = song) }

            val previewUrl = song.previewUrl
            if (previewUrl != null) {
                audioPlayer.setTrack(previewUrl)
            } else {
                _uiState.update { it.copy(error = PlayerError.NO_PREVIEW) }
            }
        }
    }

    private fun observePlayback() {
        viewModelScope.launch {
            audioPlayer.state.collect { playback ->
                _uiState.update {
                    it.copy(
                        isPlaying = playback.isPlaying,
                        isBuffering = playback.isBuffering,
                        positionMs = playback.positionMs,
                        durationMs = if (playback.durationMs > 0) playback.durationMs else it.durationMs,
                    )
                }
            }
        }
    }

    fun onSeekToFraction(fraction: Float) {
        val duration = _uiState.value.durationMs
        if (duration > 0) audioPlayer.seekTo((fraction * duration).toLong())
    }

    fun onPlayPause() = audioPlayer.playPause()

    fun onForward() = audioPlayer.seekBy(SKIP_MS)

    fun onBackward() = audioPlayer.seekBy(-SKIP_MS)

    fun onMoreOptionsVisibilityChange(visible: Boolean) = _uiState.update {
        it.copy(showMoreOptions = visible)
    }

    override fun onCleared() {
        audioPlayer.pause()
    }

    private companion object {
        const val SKIP_MS = 10_000L
    }
}
