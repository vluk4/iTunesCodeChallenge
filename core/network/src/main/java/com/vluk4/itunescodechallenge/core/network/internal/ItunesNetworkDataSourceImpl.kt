package com.vluk4.itunescodechallenge.core.network.internal

import com.vluk4.itunescodechallenge.core.network.ItunesNetworkDataSource
import com.vluk4.itunescodechallenge.core.network.model.NetworkSong
import javax.inject.Inject

internal class ItunesNetworkDataSourceImpl @Inject constructor(
    private val api: ItunesApiService,
) : ItunesNetworkDataSource {

    override suspend fun searchSongs(term: String, limit: Int): List<NetworkSong> =
        api.search(term = term, limit = limit).results

    override suspend fun lookupAlbum(collectionId: Long): List<NetworkSong> =
        // The first result is the album wrapper; the remainder are its tracks.
        api.lookup(collectionId = collectionId).results
}
