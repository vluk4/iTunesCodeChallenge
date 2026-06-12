package com.vluk4.itunescodechallenge.core.network.internal

import com.vluk4.itunescodechallenge.core.network.model.NetworkSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

internal interface ItunesApiService {

    @GET("search")
    suspend fun search(
        @Query("term") term: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song",
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): NetworkSearchResponse

    @GET("lookup")
    suspend fun lookup(
        @Query("id") collectionId: Long,
        @Query("entity") entity: String = "song",
    ): NetworkSearchResponse
}
