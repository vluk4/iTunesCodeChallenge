package com.vluk4.itunescodechallenge.feature.album

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.vluk4.itunescodechallenge.core.designsystem.theme.ITunesCodeChallengeTheme
import com.vluk4.itunescodechallenge.core.domain.model.Album
import com.vluk4.itunescodechallenge.core.domain.model.Song
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], qualifiers = "w411dp-h891dp")
class AlbumScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun track(id: Long, title: String) = Song(
        id = id,
        title = title,
        artistName = "The Beatles",
        collectionId = 1L,
        collectionName = "Abbey Road",
        artworkUrl = "",
        previewUrl = null,
        genre = null,
        trackTimeMillis = null,
        releaseDate = null,
    )

    private fun album(tracks: List<Song>) = Album(
        id = 1L,
        name = "Abbey Road",
        artistName = "The Beatles",
        artworkUrl = "",
        genre = "Rock",
        releaseDate = "1969",
        trackCount = tracks.size,
        tracks = tracks,
    )

    private fun setContent(
        state: AlbumUiState,
        onTrackClick: (Song) -> Unit = {},
        onRetry: () -> Unit = {},
    ) {
        composeRule.setContent {
            ITunesCodeChallengeTheme {
                AlbumScreen(
                    state = state,
                    onBack = {},
                    onTrackClick = onTrackClick,
                    onRetry = onRetry,
                )
            }
        }
    }

    @Test
    fun `success state shows album info and tracks`() {
        setContent(
            AlbumUiState.Success(album(listOf(track(1, "Come Together"), track(2, "Something")))),
        )

        // "Abbey Road" appears in both the top bar and the header.
        composeRule.onAllNodesWithText("Abbey Road").onFirst().assertIsDisplayed()
        composeRule.onNodeWithText("Come Together").assertIsDisplayed()
        composeRule.onNodeWithText("Something").assertIsDisplayed()
    }

    @Test
    fun `tapping a track invokes onTrackClick`() {
        var clicked: Song? = null
        val target = track(1, "Come Together")
        setContent(
            AlbumUiState.Success(album(listOf(target))),
            onTrackClick = { clicked = it },
        )

        composeRule.onNodeWithText("Come Together").performClick()

        assertEquals(target, clicked)
    }

    @Test
    fun `loading state shows a progress indicator`() {
        setContent(AlbumUiState.Loading)

        composeRule
            .onNode(SemanticsMatcher.keyIsDefined(SemanticsProperties.ProgressBarRangeInfo))
            .assertIsDisplayed()
    }

    @Test
    fun `error state shows the message and retry invokes the callback`() {
        var retried = false
        setContent(AlbumUiState.Error(message = "Network down"), onRetry = { retried = true })

        composeRule.onNodeWithText("Network down").assertIsDisplayed()
        composeRule.onNodeWithText("Retry").performClick()

        assertTrue(retried)
    }

    @Test
    fun `error state falls back to a generic message when none is provided`() {
        setContent(AlbumUiState.Error(message = null))

        composeRule.onNodeWithText("Failed to load album").assertIsDisplayed()
    }
}
