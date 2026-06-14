package com.vluk4.itunescodechallenge.feature.player.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vluk4.itunescodechallenge.core.designsystem.transition.LocalNavAnimatedVisibilityScope
import com.vluk4.itunescodechallenge.feature.player.PlayerRoute

const val PLAYER_ARG_SONG_ID = "songId"
private const val PLAYER_ROUTE = "player"
private const val TRANSITION_MS = 400

fun NavController.navigateToPlayer(songId: Long) = navigate("$PLAYER_ROUTE/$songId") {
    launchSingleTop = true
}

fun NavGraphBuilder.playerScreen(
    onBack: () -> Unit,
    onViewAlbum: (collectionId: Long) -> Unit,
) {
    composable(
        route = "$PLAYER_ROUTE/{$PLAYER_ARG_SONG_ID}",
        arguments = listOf(navArgument(PLAYER_ARG_SONG_ID) { type = NavType.LongType }),
        enterTransition = { fadeIn(animationSpec = tween(TRANSITION_MS)) },
        popExitTransition = { fadeOut(animationSpec = tween(TRANSITION_MS)) },
    ) {
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            PlayerRoute(onBack = onBack, onViewAlbum = onViewAlbum)
        }
    }
}
