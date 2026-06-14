package com.vluk4.itunescodechallenge.feature.player.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vluk4.itunescodechallenge.feature.player.PlayerRoute

const val PLAYER_ARG_SONG_ID = "songId"
private const val PLAYER_ROUTE = "player"

fun NavController.navigateToPlayer(songId: Long) = navigate("$PLAYER_ROUTE/$songId")

fun NavGraphBuilder.playerScreen(
    onBack: () -> Unit,
    onViewAlbum: (collectionId: Long) -> Unit,
) {
    composable(
        route = "$PLAYER_ROUTE/{$PLAYER_ARG_SONG_ID}",
        arguments = listOf(navArgument(PLAYER_ARG_SONG_ID) { type = NavType.LongType }),
    ) {
        PlayerRoute(onBack = onBack, onViewAlbum = onViewAlbum)
    }
}
