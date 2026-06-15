package com.vluk4.itunescodechallenge.feature.player

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.vluk4.itunescodechallenge.core.designsystem.theme.ITunesCodeChallengeTheme
import com.vluk4.itunescodechallenge.core.domain.model.Song
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], qualifiers = "w411dp-h891dp")
class PlayerScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun song() = Song(
        id = 1L,
        title = "Get Lucky",
        artistName = "Daft Punk",
        collectionId = 10L,
        collectionName = "Random Access Memories",
        artworkUrl = "",
        previewUrl = "https://example.com/preview.m4a",
        genre = null,
        trackTimeMillis = 30_000,
        releaseDate = null,
    )

    private fun setContent(state: PlayerUiState, onPlayPause: () -> Unit = {}) {
        composeRule.setContent {
            ITunesCodeChallengeTheme {
                PlayerScreen(
                    state = state,
                    onBack = {},
                    onSeekToFraction = {},
                    onPlayPause = onPlayPause,
                    onForward = {},
                    onBackward = {},
                    onToggleRepeat = {},
                    onMoreOptionsVisibilityChange = {},
                    onViewAlbum = {},
                )
            }
        }
    }

    @Test
    fun `renders the song title and artist`() {
        setContent(PlayerUiState(isLoading = false, song = song(), durationMs = 30_000))

        composeRule.onNodeWithText("Get Lucky").assertIsDisplayed()
        composeRule.onNodeWithText("Daft Punk").assertIsDisplayed()
    }

    @Test
    fun `shows the play control while paused and forwards taps`() {
        var played = false
        setContent(PlayerUiState(isLoading = false, song = song(), isPlaying = false)) {
            played = true
        }

        composeRule.onNodeWithContentDescription("Play").assertIsDisplayed().performClick()

        assertTrue(played)
    }

    @Test
    fun `shows the pause control while playing`() {
        setContent(PlayerUiState(isLoading = false, song = song(), isPlaying = true))

        composeRule.onNodeWithContentDescription("Pause").assertIsDisplayed()
    }

    @Test
    fun `shows an error message when the preview is unavailable`() {
        setContent(PlayerUiState(isLoading = false, song = null, error = PlayerError.NO_PREVIEW))

        composeRule.onNodeWithText("No preview available for this song").assertIsDisplayed()
    }
}
