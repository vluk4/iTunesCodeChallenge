package com.vluk4.itunescodechallenge.core.data.mapper

import com.vluk4.itunescodechallenge.core.network.model.NetworkSong
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SongMappersTest {

    @Test
    fun `toEntityOrNull returns null when track id is missing`() {
        val dto = NetworkSong(trackId = null, trackName = "Yesterday")

        assertNull(dto.toEntityOrNull(searchQuery = "beatles", sortIndex = 0))
    }

    @Test
    fun `toEntityOrNull upgrades artwork to high resolution`() {
        val dto = NetworkSong(
            trackId = 1L,
            trackName = "Yesterday",
            artworkUrl100 = "https://example.com/100x100bb.jpg",
        )

        val entity = dto.toEntityOrNull(searchQuery = "beatles", sortIndex = 2)

        assertEquals("https://example.com/600x600bb.jpg", entity?.artworkUrl)
        assertEquals(2, entity?.sortIndex)
        assertEquals("beatles", entity?.searchQuery)
    }

    @Test
    fun `entity round-trips to domain`() {
        val dto = NetworkSong(trackId = 5L, trackName = "Let It Be", artistName = "The Beatles")
        val entity = dto.toEntityOrNull(searchQuery = null, sortIndex = null)!!

        val song = entity.toDomain()

        assertEquals(5L, song.id)
        assertEquals("Let It Be", song.title)
        assertEquals("The Beatles", song.artistName)
    }
}
