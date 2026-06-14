package com.vluk4.itunescodechallenge.feature.songs

import androidx.compose.ui.tooling.preview.Preview
import com.vluk4.itunescodechallenge.core.designsystem.theme.ITunesCodeChallengeTheme
import androidx.paging.PagingData
import kotlinx.coroutines.flow.flowOf
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.vluk4.itunescodechallenge.core.designsystem.component.MediaRow
import com.vluk4.itunescodechallenge.core.designsystem.component.MoreOptionsBottomSheet
import com.vluk4.itunescodechallenge.core.domain.model.Song
import com.vluk4.itunescodechallenge.feature.songs.R

@Composable
fun SongsRoute(
    onSongClick: (Song) -> Unit,
    onViewAlbum: (collectionId: Long) -> Unit,
    viewModel: SongsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val recentlyPlayed by viewModel.recentlyPlayed.collectAsStateWithLifecycle()
    val songs = viewModel.songs.collectAsLazyPagingItems()

    SongsScreen(
        uiState = uiState,
        recentlyPlayed = recentlyPlayed,
        songs = songs,
        onQueryChange = viewModel::onQueryChange,
        onSongClick = onSongClick,
        onSongSelected = viewModel::onSongSelected,
        onMoreOptionsDismissed = viewModel::onMoreOptionsDismissed,
        onViewAlbum = { collectionId ->
            viewModel.onMoreOptionsDismissed()
            onViewAlbum(collectionId)
        },
    )
}

@Composable
internal fun SongsScreen(
    uiState: SongsUiState,
    recentlyPlayed: List<Song>,
    songs: LazyPagingItems<Song>,
    onQueryChange: (String) -> Unit,
    onSongClick: (Song) -> Unit,
    onSongSelected: (Song) -> Unit,
    onMoreOptionsDismissed: () -> Unit,
    onViewAlbum: (Long) -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text(
                text = stringResource(R.string.songs_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 24.dp, top = 16.dp, bottom = 12.dp),
            )

            SearchField(query = uiState.query, onQueryChange = onQueryChange)

            // Default to recently-played (offline-first); show search results when typing.
            if (!uiState.isSearching) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(recentlyPlayed, key = { it.id }) { song ->
                        SongRow(song, onSongClick, onMore = { onSongSelected(song) })
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        count = songs.itemCount,
                        key = songs.itemKey { it.id },
                    ) { index ->
                        songs[index]?.let { song ->
                            SongRow(song, onSongClick, onMore = { onSongSelected(song) })
                        }
                    }
                    if (songs.loadState.append is LoadState.Loading ||
                        songs.loadState.refresh is LoadState.Loading
                    ) {
                        item { LoadingRow() }
                    }
                }
            }
        }
    }

    if (uiState.isMoreOptionsVisible && uiState.selectedSong != null) {
        MoreOptionsBottomSheet(
            title = uiState.selectedSong.title,
            subtitle = uiState.selectedSong.artistName,
            onViewAlbum = { onViewAlbum(uiState.selectedSong.collectionId) },
            onDismiss = onMoreOptionsDismissed,
        )
    }
}

@Composable
private fun SongRow(song: Song, onClick: (Song) -> Unit, onMore: () -> Unit) {
    MediaRow(
        title = song.title,
        subtitle = song.artistName,
        artworkUrl = song.artworkUrl,
        onClick = { onClick(song) },
        onMoreClick = onMore,
    )
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        placeholder = { Text(stringResource(R.string.search_placeholder)) },
        shape = RoundedCornerShape(28.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 4.dp),
    )
}

@Composable
private fun LoadingRow() {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
private fun SongsScreenPreview() {
    val fakeSongs = listOf(
        Song(1, "Song 1", "Artist 1", 1, "Collection", "url", null, null, null, null),
        Song(2, "Song 2", "Artist 1", 1, "Collection", "url", null, null, null, null)
    )
    ITunesCodeChallengeTheme {
        SongsScreen(
            uiState = SongsUiState(query = ""),
            recentlyPlayed = fakeSongs,
            songs = flowOf(PagingData.empty<Song>()).collectAsLazyPagingItems(),
            onQueryChange = {},
            onSongClick = {},
            onSongSelected = {},
            onMoreOptionsDismissed = {},
            onViewAlbum = {}
        )
    }
}
