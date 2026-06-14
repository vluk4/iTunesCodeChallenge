package com.vluk4.itunescodechallenge.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vluk4.itunescodechallenge.core.designsystem.transition.LocalSharedTransitionScope
import com.vluk4.itunescodechallenge.feature.album.navigation.albumScreen
import com.vluk4.itunescodechallenge.feature.album.navigation.navigateToAlbum
import com.vluk4.itunescodechallenge.feature.player.navigation.navigateToPlayer
import com.vluk4.itunescodechallenge.feature.player.navigation.playerScreen
import com.vluk4.itunescodechallenge.feature.songs.navigation.SONGS_ROUTE
import com.vluk4.itunescodechallenge.feature.songs.navigation.songsScreen
import com.vluk4.itunescodechallenge.splash.SplashScreen

private const val SPLASH_ROUTE = "splash"

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ItunesNavHost(
    navController: NavHostController = rememberNavController(),
) {
    SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            NavHost(
                navController = navController,
                startDestination = SPLASH_ROUTE,
            ) {
                composable(SPLASH_ROUTE) {
                    SplashScreen(
                        onTimeout = {
                            navController.navigate(SONGS_ROUTE) {
                                popUpTo(SPLASH_ROUTE) { inclusive = true }
                            }
                        },
                    )
                }
                songsScreen(
                    onSongClick = { song -> navController.navigateIfResumed { navigateToPlayer(song.id) } },
                    onViewAlbum = { collectionId -> navController.navigateIfResumed { navigateToAlbum(collectionId) } },
                )
                playerScreen(
                    onBack = navController::popBackStack,
                    onViewAlbum = { collectionId -> navController.navigateIfResumed { navigateToAlbum(collectionId) } },
                )
                albumScreen(
                    onBack = navController::popBackStack,
                    onTrackClick = { song -> navController.navigateIfResumed { navigateToPlayer(song.id) } },
                )
            }
        }
    }
}

/**
 * Runs [navigate] only while the current destination is still RESUMED. Once a
 * navigation starts, the source entry drops below RESUMED for the duration of the
 * transition, so a second tap (e.g. a fast double-tap on a row) is ignored instead
 * of pushing a duplicate destination onto the back stack.
 */
private fun NavHostController.navigateIfResumed(navigate: NavHostController.() -> Unit) {
    val isResumed = currentBackStackEntry?.lifecycle?.currentState
        ?.isAtLeast(Lifecycle.State.RESUMED) == true
    if (isResumed) navigate()
}
