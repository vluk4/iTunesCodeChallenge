package com.vluk4.itunescodechallenge.core.testing

import com.vluk4.itunescodechallenge.core.domain.player.AudioPlayer
import com.vluk4.itunescodechallenge.core.domain.player.PlaybackState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeAudioPlayer : AudioPlayer {

    private val _state = MutableStateFlow(PlaybackState())
    override val state: StateFlow<PlaybackState> = _state.asStateFlow()

    var lastUrl: String? = null
        private set

    override fun setTrack(url: String) {
        lastUrl = url
        _state.update { it.copy(isPlaying = true, durationMs = 30_000L) }
    }

    override fun playPause() = _state.update { it.copy(isPlaying = !it.isPlaying) }

    override fun pause() = _state.update { it.copy(isPlaying = false) }

    override fun seekTo(positionMs: Long) = _state.update { it.copy(positionMs = positionMs) }

    override fun seekBy(deltaMs: Long) = _state.update {
        it.copy(positionMs = (it.positionMs + deltaMs).coerceAtLeast(0L))
    }

    override fun release() = Unit
}
