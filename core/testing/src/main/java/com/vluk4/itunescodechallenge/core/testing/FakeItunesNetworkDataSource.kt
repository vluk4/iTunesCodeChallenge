package com.vluk4.itunescodechallenge.core.testing

import com.vluk4.itunescodechallenge.core.network.ItunesNetworkDataSource
import com.vluk4.itunescodechallenge.core.network.model.NetworkSong

class FakeItunesNetworkDataSource : ItunesNetworkDataSource {

    var songs: List<NetworkSong> = emptyList()
    var albumTracks: List<NetworkSong> = emptyList()
    var errorToThrow: Throwable? = null

    override suspend fun searchSongs(term: String, limit: Int): List<NetworkSong> {
        errorToThrow?.let { throw it }
        return songs.take(limit)
    }

    override suspend fun lookupAlbum(collectionId: Long): List<NetworkSong> {
        errorToThrow?.let { throw it }
        return albumTracks
    }
}
