package com.vluk4.itunescodechallenge.feature.player

import androidx.compose.ui.tooling.preview.Preview
import com.vluk4.itunescodechallenge.core.designsystem.theme.ITunesCodeChallengeTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vluk4.itunescodechallenge.core.designsystem.component.Artwork
import com.vluk4.itunescodechallenge.core.designsystem.component.MoreOptionsBottomSheet
import com.vluk4.itunescodechallenge.core.designsystem.transition.artworkSharedKey
import com.vluk4.itunescodechallenge.core.domain.model.Song
import com.vluk4.itunescodechallenge.feature.player.R

@Composable
fun PlayerRoute(
    onBack: () -> Unit,
    onViewAlbum: (collectionId: Long) -> Unit,
    viewModel: PlayerViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    PlayerScreen(
        state = state,
        onBack = onBack,
        onSeekToFraction = viewModel::onSeekToFraction,
        onPlayPause = viewModel::onPlayPause,
        onForward = viewModel::onForward,
        onBackward = viewModel::onBackward,
        onToggleRepeat = viewModel::onToggleRepeat,
        onMoreOptionsVisibilityChange = viewModel::onMoreOptionsVisibilityChange,
        onViewAlbum = onViewAlbum,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PlayerScreen(
    state: PlayerUiState,
    onBack: () -> Unit,
    onSeekToFraction: (Float) -> Unit,
    onPlayPause: () -> Unit,
    onForward: () -> Unit,
    onBackward: () -> Unit,
    onToggleRepeat: () -> Unit,
    onMoreOptionsVisibilityChange: (Boolean) -> Unit,
    onViewAlbum: (Long) -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.now_playing), style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    IconButton(onClick = { onMoreOptionsVisibilityChange(true) }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = stringResource(R.string.more_options))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.song != null -> PlayerContent(
                    song = state.song,
                    progress = state.progress,
                    positionLabel = state.positionLabel,
                    durationLabel = state.durationLabel,
                    isPlaying = state.isPlaying,
                    isRepeatEnabled = state.isRepeatEnabled,
                    onSeekToFraction = onSeekToFraction,
                    onPlayPause = onPlayPause,
                    onForward = onForward,
                    onBackward = onBackward,
                    onToggleRepeat = onToggleRepeat,
                )

                else -> Text(stringResource(playerErrorTextRes(state.error)))
            }
        }
    }

    val song = state.song
    if (state.showMoreOptions && song != null) {
        MoreOptionsBottomSheet(
            title = song.title,
            subtitle = song.artistName,
            onViewAlbum = {
                onMoreOptionsVisibilityChange(false)
                onViewAlbum(song.collectionId)
            },
            onDismiss = { onMoreOptionsVisibilityChange(false) },
        )
    }
}

@Composable
private fun PlayerContent(
    song: Song,
    progress: Float,
    positionLabel: String,
    durationLabel: String,
    isPlaying: Boolean,
    isRepeatEnabled: Boolean,
    onSeekToFraction: (Float) -> Unit,
    onPlayPause: () -> Unit,
    onForward: () -> Unit,
    onBackward: () -> Unit,
    onToggleRepeat: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            Artwork(
                url = song.artworkUrl,
                contentDescription = song.title,
                size = 280.dp,
                cornerRadius = 16.dp,
                modifier = Modifier.aspectRatio(1f),
                sharedKey = artworkSharedKey(song.id),
            )
        }

        Spacer(Modifier.height(32.dp))
        Text(
            text = song.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = song.artistName,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.height(16.dp))
        // Timeline is mandatory; drag-to-seek is wired through onSeekToFraction.
        Slider(
            value = progress,
            onValueChange = onSeekToFraction,
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = positionLabel,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = durationLabel,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(Modifier.height(24.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            FilledIconButton(onClick = onPlayPause, modifier = Modifier.size(56.dp)) {
                Icon(
                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = stringResource(
                        if (isPlaying) R.string.action_pause else R.string.action_play,
                    ),
                )
            }
            Spacer(Modifier.size(8.dp))
            IconButton(onClick = onBackward) {
                Icon(Icons.Filled.SkipPrevious, contentDescription = stringResource(R.string.action_previous))
            }
            IconButton(onClick = onForward) {
                Icon(Icons.Filled.SkipNext, contentDescription = stringResource(R.string.action_next))
            }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onToggleRepeat) {
                Icon(
                    Icons.Filled.Repeat,
                    contentDescription = stringResource(R.string.action_repeat),
                    tint = if (isRepeatEnabled) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )
            }
        }
    }
}

private fun playerErrorTextRes(error: PlayerError?): Int = when (error) {
    PlayerError.SONG_UNAVAILABLE -> R.string.error_song_unavailable
    PlayerError.NO_PREVIEW -> R.string.error_no_preview
    null -> R.string.error_generic
}

@Preview
@Composable
private fun PlayerScreenPreview() {
    val fakeSong = Song(1, "Song Title", "Artist Name", 1, "Collection", "url", null, null, null, null)
    ITunesCodeChallengeTheme {
        PlayerScreen(
            state = PlayerUiState(
                isLoading = false,
                song = fakeSong,
                positionMs = 30000,
                durationMs = 180000,
                isPlaying = true
            ),
            onBack = {},
            onSeekToFraction = {},
            onPlayPause = {},
            onForward = {},
            onBackward = {},
            onToggleRepeat = {},
            onMoreOptionsVisibilityChange = {},
            onViewAlbum = {}
        )
    }
}
