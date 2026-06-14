package com.vluk4.itunescodechallenge.feature.player

import com.vluk4.itunescodechallenge.core.domain.model.Song
import java.util.Locale

data class PlayerUiState(
    val isLoading: Boolean = true,
    val song: Song? = null,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val showMoreOptions: Boolean = false,
    val error: PlayerError? = null,
) {
    /** Playback progress in [0f, 1f]; 0 while the duration is still unknown. */
    val progress: Float
        get() = if (durationMs > 0) (positionMs.toFloat() / durationMs).coerceIn(0f, 1f) else 0f

    val positionLabel: String get() = formatTime(positionMs)

    val durationLabel: String get() = formatTime(durationMs)
}

private fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    return String.format(Locale.US, "%d:%02d", totalSeconds / 60, totalSeconds % 60)
}

enum class PlayerError {
    SONG_UNAVAILABLE,
    NO_PREVIEW,
}
