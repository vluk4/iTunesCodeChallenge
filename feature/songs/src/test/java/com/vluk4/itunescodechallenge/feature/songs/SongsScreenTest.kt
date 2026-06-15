package com.vluk4.itunescodechallenge.feature.songs

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.vluk4.itunescodechallenge.core.designsystem.theme.ITunesCodeChallengeTheme
import com.vluk4.itunescodechallenge.core.domain.model.Song
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SongsScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun song(id: Long, title: String) = Song(
        id = id,
        title = title,
        artistName = "Artist $id",
        collectionId = 1L,
        collectionName = "Collection",
        artworkUrl = "",
        previewUrl = null,
        genre = null,
        trackTimeMillis = null,
        releaseDate = null,
    )

    private fun setContent(
        uiState: SongsUiState,
        recentlyPlayed: List<Song> = emptyList(),
        searchResults: List<Song> = emptyList(),
        onQueryChange: (String) -> Unit = {},
        onSongClick: (Song) -> Unit = {},
        onSongSelected: (Song) -> Unit = {},
    ) {
        composeRule.setContent {
            ITunesCodeChallengeTheme {
                SongsScreen(
                    uiState = uiState,
                    recentlyPlayed = recentlyPlayed,
                    songs = flowOf(PagingData.from(searchResults)).collectAsLazyPagingItems(),
                    onQueryChange = onQueryChange,
                    onSongClick = onSongClick,
                    onSongSelected = onSongSelected,
                    onMoreOptionsDismissed = {},
                    onViewAlbum = {},
                )
            }
        }
    }

    @Test
    fun `shows title and recently played when query is blank`() {
        setContent(
            uiState = SongsUiState(query = ""),
            recentlyPlayed = listOf(song(1, "First Song"), song(2, "Second Song")),
        )

        composeRule.onNodeWithText("Songs").assertIsDisplayed()
        composeRule.onNodeWithText("First Song").assertIsDisplayed()
        composeRule.onNodeWithText("Second Song").assertIsDisplayed()
    }

    @Test
    fun `typing in the search field emits the query`() {
        var typed: String? = null
        setContent(uiState = SongsUiState(query = ""), onQueryChange = { typed = it })

        composeRule.onNode(hasSetTextAction()).performTextInput("beatles")

        assertEquals("beatles", typed)
    }

    @Test
    fun `tapping a recently played row invokes onSongClick`() {
        var clicked: Song? = null
        val target = song(7, "Tap Me")
        setContent(
            uiState = SongsUiState(query = ""),
            recentlyPlayed = listOf(target),
            onSongClick = { clicked = it },
        )

        composeRule.onNodeWithText("Tap Me").performClick()

        assertEquals(target, clicked)
    }

    @Test
    fun `clear button resets the query when text is present`() {
        var typed: String? = null
        setContent(uiState = SongsUiState(query = "beatles"), onQueryChange = { typed = it })

        composeRule.onNodeWithContentDescription("Clear search").performClick()

        assertEquals("", typed)
    }
}
