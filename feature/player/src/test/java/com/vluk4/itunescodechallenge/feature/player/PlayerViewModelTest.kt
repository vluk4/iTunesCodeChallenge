package com.vluk4.itunescodechallenge.feature.player

import androidx.lifecycle.SavedStateHandle
import com.vluk4.itunescodechallenge.core.domain.model.Song
import com.vluk4.itunescodechallenge.core.domain.usecase.GetSongUseCase
import com.vluk4.itunescodechallenge.core.domain.usecase.MarkSongAsPlayedUseCase
import com.vluk4.itunescodechallenge.core.testing.FakeAudioPlayer
import com.vluk4.itunescodechallenge.core.testing.FakeSongRepository
import com.vluk4.itunescodechallenge.core.testing.MainDispatcherRule
import com.vluk4.itunescodechallenge.feature.player.navigation.PLAYER_ARG_SONG_ID
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PlayerViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeSongRepository()
    private val audioPlayer = FakeAudioPlayer()

    private fun viewModel() = PlayerViewModel(
        getSong = GetSongUseCase(repository),
        markAsPlayed = MarkSongAsPlayedUseCase(repository),
        audioPlayer = audioPlayer,
        savedStateHandle = SavedStateHandle(mapOf(PLAYER_ARG_SONG_ID to 1L)),
    )

    private fun song(previewUrl: String?) = Song(
        id = 1L,
        title = "Get Lucky",
        artistName = "Daft Punk",
        collectionId = 10L,
        collectionName = "Random Access Memories",
        artworkUrl = "",
        previewUrl = previewUrl,
        genre = "Pop",
        trackTimeMillis = 30_000,
        releaseDate = null,
    )

    @Test
    fun `loads the song and starts streaming its preview`() = runTest {
        repository.searchResults = listOf(song("https://example.com/preview.m4a"))

        val viewModel = viewModel()

        assertEquals("Get Lucky", viewModel.uiState.value.song?.title)
        assertEquals("https://example.com/preview.m4a", audioPlayer.lastUrl)
        assertTrue(viewModel.uiState.value.isPlaying)
    }

    @Test
    fun `onPlayPause toggles playback state`() = runTest {
        repository.searchResults = listOf(song("https://example.com/preview.m4a"))
        val viewModel = viewModel()

        viewModel.onPlayPause()

        assertFalse(viewModel.uiState.value.isPlaying)
    }

    @Test
    fun `reports an error when the song has no preview`() = runTest {
        repository.searchResults = listOf(song(previewUrl = null))

        val viewModel = viewModel()

        assertEquals(PlayerError.NO_PREVIEW, viewModel.uiState.value.error)
    }

    @Test
    fun `onSeekToFraction maps the fraction to an absolute position`() = runTest {
        repository.searchResults = listOf(song("https://example.com/preview.m4a"))
        val viewModel = viewModel()

        // FakeAudioPlayer reports a 30s duration once a track is set.
        viewModel.onSeekToFraction(0.5f)

        assertEquals(15_000L, viewModel.uiState.value.positionMs)
    }

    @Test
    fun `derives progress fraction and time labels from position and duration`() {
        val state = PlayerUiState(durationMs = 30_000L, positionMs = 15_000L)

        assertEquals(0.5f, state.progress, 0.0001f)
        assertEquals("0:15", state.positionLabel)
        assertEquals("0:30", state.durationLabel)
    }

    @Test
    fun `progress is zero while duration is unknown`() {
        val state = PlayerUiState(durationMs = 0L, positionMs = 5_000L)

        assertEquals(0f, state.progress, 0.0001f)
    }
}
