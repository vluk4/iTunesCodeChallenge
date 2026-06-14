package com.vluk4.itunescodechallenge.core.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.vluk4.itunescodechallenge.core.domain.player.AudioPlayer
import com.vluk4.itunescodechallenge.core.domain.player.PlaybackState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ExoAudioPlayer @Inject constructor(
    @ApplicationContext context: Context,
) : AudioPlayer {

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val _state = MutableStateFlow(PlaybackState())
    override val state: StateFlow<PlaybackState> = _state.asStateFlow()

    private val player: ExoPlayer = ExoPlayer.Builder(context).build().apply {
        addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _state.update { it.copy(isPlaying = isPlaying) }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                _state.update {
                    it.copy(
                        isBuffering = playbackState == Player.STATE_BUFFERING,
                        durationMs = positiveDurationOr(it.durationMs),
                    )
                }
            }
        })
    }

    private var pollingJob: Job? = null

    init {
        pollingJob = scope.launch {
            while (isActive) {
                if (player.isPlaying) {
                    _state.update {
                        it.copy(
                            positionMs = player.currentPosition,
                            durationMs = positiveDurationOr(it.durationMs),
                        )
                    }
                }
                delay(POLL_INTERVAL_MS)
            }
        }
    }

    override fun setTrack(url: String) {
        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
        player.playWhenReady = true
    }

    override fun playPause() {
        if (player.isPlaying) player.pause() else player.play()
    }

    override fun pause() = player.pause()

    override fun seekTo(positionMs: Long) {
        val clamped = positionMs.coerceIn(0L, positiveDurationOr(Long.MAX_VALUE))
        player.seekTo(clamped)
        _state.update { it.copy(positionMs = clamped) }
    }

    override fun seekBy(deltaMs: Long) = seekTo(player.currentPosition + deltaMs)

    override fun release() {
        pollingJob?.cancel()
        player.release()
    }

    /** ExoPlayer returns C.TIME_UNSET while a duration is unknown. */
    private fun positiveDurationOr(fallback: Long): Long =
        player.duration.takeIf { it > 0 } ?: fallback

    private companion object {
        const val POLL_INTERVAL_MS = 500L
    }
}
