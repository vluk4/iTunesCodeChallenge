package com.vluk4.itunescodechallenge.splash

import androidx.compose.ui.tooling.preview.Preview
import com.vluk4.itunescodechallenge.core.designsystem.theme.ITunesCodeChallengeTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vluk4.itunescodechallenge.R
import com.vluk4.itunescodechallenge.core.designsystem.theme.SplashGradientEnd
import com.vluk4.itunescodechallenge.core.designsystem.theme.SplashGradientStart
import kotlinx.coroutines.delay

private const val SPLASH_DURATION_MS = 1_500L

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(SPLASH_DURATION_MS)
        onTimeout()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(SplashGradientStart, SplashGradientEnd),
                    start = Offset.Zero,
                    end = Offset.Infinite,
                ),
            ),
    ) {
        Image(
            painter = painterResource(R.drawable.splash_music_note),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    ITunesCodeChallengeTheme {
        SplashScreen(onTimeout = {})
    }
}
