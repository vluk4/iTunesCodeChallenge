package com.vluk4.itunescodechallenge.core.domain.player

import kotlinx.coroutines.flow.StateFlow

data class PlaybackState(
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
)

interface AudioPlayer {
    val state: StateFlow<PlaybackState>

    fun setTrack(url: String)

    fun playPause()

    fun pause()

    fun seekTo(positionMs: Long)

    fun seekBy(deltaMs: Long)

    fun release()
}
