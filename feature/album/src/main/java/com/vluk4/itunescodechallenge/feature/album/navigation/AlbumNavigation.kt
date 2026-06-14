package com.vluk4.itunescodechallenge.feature.album.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vluk4.itunescodechallenge.core.domain.model.Song
import com.vluk4.itunescodechallenge.feature.album.AlbumRoute

const val ALBUM_ARG_COLLECTION_ID = "collectionId"
private const val ALBUM_ROUTE = "album"

fun NavController.navigateToAlbum(collectionId: Long) = navigate("$ALBUM_ROUTE/$collectionId") {
    launchSingleTop = true
}

fun NavGraphBuilder.albumScreen(
    onBack: () -> Unit,
    onTrackClick: (Song) -> Unit,
) {
    composable(
        route = "$ALBUM_ROUTE/{$ALBUM_ARG_COLLECTION_ID}",
        arguments = listOf(navArgument(ALBUM_ARG_COLLECTION_ID) { type = NavType.LongType }),
    ) {
        AlbumRoute(onBack = onBack, onTrackClick = onTrackClick)
    }
}
