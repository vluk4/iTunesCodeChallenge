package com.vluk4.itunescodechallenge.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vluk4.itunescodechallenge.feature.album.navigation.albumScreen
import com.vluk4.itunescodechallenge.feature.album.navigation.navigateToAlbum
import com.vluk4.itunescodechallenge.feature.player.navigation.navigateToPlayer
import com.vluk4.itunescodechallenge.feature.player.navigation.playerScreen
import com.vluk4.itunescodechallenge.feature.songs.navigation.SONGS_ROUTE
import com.vluk4.itunescodechallenge.feature.songs.navigation.songsScreen
import com.vluk4.itunescodechallenge.splash.SplashScreen

private const val SPLASH_ROUTE = "splash"

@Composable
fun ItunesNavHost(
    navController: NavHostController = rememberNavController(),
) {
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
            onSongClick = { song -> navController.navigateToPlayer(song.id) },
            onViewAlbum = { collectionId -> navController.navigateToAlbum(collectionId) },
        )
        playerScreen(
            onBack = navController::popBackStack,
            onViewAlbum = { collectionId -> navController.navigateToAlbum(collectionId) },
        )
        albumScreen(
            onBack = navController::popBackStack,
            onTrackClick = { song -> navController.navigateToPlayer(song.id) },
        )
    }
}
