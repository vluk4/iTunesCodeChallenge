package com.vluk4.itunescodechallenge.feature.songs.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vluk4.itunescodechallenge.core.domain.model.Song
import com.vluk4.itunescodechallenge.feature.songs.SongsRoute

const val SONGS_ROUTE = "songs"

fun NavGraphBuilder.songsScreen(
    onSongClick: (Song) -> Unit,
    onViewAlbum: (collectionId: Long) -> Unit,
) {
    composable(route = SONGS_ROUTE) {
        SongsRoute(onSongClick = onSongClick, onViewAlbum = onViewAlbum)
    }
}
