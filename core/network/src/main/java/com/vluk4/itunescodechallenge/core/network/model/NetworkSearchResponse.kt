package com.vluk4.itunescodechallenge.core.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class NetworkSearchResponse(
    @SerialName("resultCount") val resultCount: Int = 0,
    @SerialName("results") val results: List<NetworkSong> = emptyList(),
)
