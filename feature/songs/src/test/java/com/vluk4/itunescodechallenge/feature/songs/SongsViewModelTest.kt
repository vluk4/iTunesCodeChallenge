package com.vluk4.itunescodechallenge.feature.songs

import app.cash.turbine.test
import com.vluk4.itunescodechallenge.core.domain.model.Song
import com.vluk4.itunescodechallenge.core.domain.usecase.GetRecentlyPlayedSongsUseCase
import com.vluk4.itunescodechallenge.core.domain.usecase.SearchSongsUseCase
import com.vluk4.itunescodechallenge.core.testing.FakeSongRepository
import com.vluk4.itunescodechallenge.core.testing.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SongsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeSongRepository()

    private fun viewModel() = SongsViewModel(
        searchSongs = SearchSongsUseCase(repository),
        getRecentlyPlayed = GetRecentlyPlayedSongsUseCase(repository),
    )

    @Test
    fun `onQueryChange updates the query state`() = runTest {
        val viewModel = viewModel()

        viewModel.onQueryChange("beatles")

        assertEquals("beatles", viewModel.uiState.value.query)
    }

    @Test
    fun `recentlyPlayed reflects repository updates`() = runTest {
        val song = sampleSong()
        val viewModel = viewModel()

        viewModel.recentlyPlayed.test {
            assertEquals(emptyList<Song>(), awaitItem())
            repository.markAsPlayed(song)
            assertEquals(listOf(song), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSongSelected sets selectedSong and shows bottom sheet`() = runTest {
        val song = sampleSong()
        val viewModel = viewModel()

        viewModel.onSongSelected(song)

        with(viewModel.uiState.value) {
            assertEquals(song, selectedSong)
            assertEquals(true, isMoreOptionsVisible)
        }
    }

    @Test
    fun `onMoreOptionsDismissed clears selectedSong and hides bottom sheet`() = runTest {
        val song = sampleSong()
        val viewModel = viewModel()

        viewModel.onSongSelected(song)
        viewModel.onMoreOptionsDismissed()

        with(viewModel.uiState.value) {
            assertEquals(null, selectedSong)
            assertEquals(false, isMoreOptionsVisible)
        }
    }

    private fun sampleSong() = Song(
        id = 1L,
        title = "Hey Jude",
        artistName = "The Beatles",
        collectionId = 10L,
        collectionName = "Singles",
        artworkUrl = "",
        previewUrl = null,
        genre = "Rock",
        trackTimeMillis = 431_000,
        releaseDate = null,
    )
}
