package com.vluk4.itunescodechallenge.feature.album

import androidx.compose.ui.tooling.preview.Preview
import com.vluk4.itunescodechallenge.core.designsystem.theme.ITunesCodeChallengeTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.vluk4.itunescodechallenge.core.designsystem.component.MediaRow
import com.vluk4.itunescodechallenge.core.domain.model.Album
import com.vluk4.itunescodechallenge.core.domain.model.Song
import com.vluk4.itunescodechallenge.feature.album.R

@Composable
fun AlbumRoute(
    onBack: () -> Unit,
    onTrackClick: (Song) -> Unit,
    viewModel: AlbumViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    AlbumScreen(
        state = state,
        onBack = onBack,
        onTrackClick = onTrackClick,
        onRetry = viewModel::load,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AlbumScreen(
    state: AlbumUiState,
    onBack: () -> Unit,
    onTrackClick: (Song) -> Unit,
    onRetry: () -> Unit,
) {
    val title = (state as? AlbumUiState.Success)?.album?.name
        ?: stringResource(R.string.album_title_fallback)
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(title, style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
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
            when (state) {
                AlbumUiState.Loading -> CircularProgressIndicator()
                is AlbumUiState.Error -> ErrorState(state.message, onRetry)
                is AlbumUiState.Success -> AlbumContent(state.album, onTrackClick)
            }
        }
    }
}

@Composable
private fun AlbumContent(album: Album, onTrackClick: (Song) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                Artwork(
                    url = album.artworkUrl,
                    contentDescription = album.name,
                    size = 160.dp,
                    cornerRadius = 16.dp,
                )
                Text(
                    text = album.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 12.dp),
                )
                Text(
                    text = album.artistName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        items(album.tracks, key = { it.id }) { track ->
            MediaRow(
                title = track.title,
                subtitle = track.artistName,
                artworkUrl = track.artworkUrl,
                onClick = { onTrackClick(track) },
            )
        }
    }
}

@Composable
private fun ErrorState(message: String?, onRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(24.dp),
    ) {
        Text(
            message ?: stringResource(R.string.error_generic),
            color = MaterialTheme.colorScheme.onBackground
        )
        Button(onClick = onRetry) { Text(stringResource(R.string.retry)) }
    }
}

@Preview
@Composable
private fun AlbumScreenPreview() {
    val fakeSongs = listOf(
        Song(1, "Song 1", "Artist 1", 1, "Collection", "url", null, null, null, null),
        Song(2, "Song 2", "Artist 1", 1, "Collection", "url", null, null, null, null)
    )
    val fakeAlbum = Album(1, "Greatest Hits", "Artist 1", "url", "Pop", "2023", 2, fakeSongs)

    ITunesCodeChallengeTheme {
        AlbumScreen(
            state = AlbumUiState.Success(fakeAlbum),
            onBack = {},
            onTrackClick = {},
            onRetry = {},
        )
    }
}
