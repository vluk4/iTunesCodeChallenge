package com.vluk4.itunescodechallenge.core.network

import com.vluk4.itunescodechallenge.core.network.model.NetworkSong

interface ItunesNetworkDataSource {

    suspend fun searchSongs(term: String, limit: Int, offset: Int): List<NetworkSong>

    suspend fun lookupAlbum(collectionId: Long): List<NetworkSong>
}
