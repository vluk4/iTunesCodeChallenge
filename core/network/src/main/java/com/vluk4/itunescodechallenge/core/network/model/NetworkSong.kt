package com.vluk4.itunescodechallenge.core.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class NetworkSong(
    @SerialName("trackId") val trackId: Long? = null,
    @SerialName("trackName") val trackName: String? = null,
    @SerialName("artistName") val artistName: String? = null,
    @SerialName("collectionId") val collectionId: Long? = null,
    @SerialName("collectionName") val collectionName: String? = null,
    @SerialName("artworkUrl100") val artworkUrl100: String? = null,
    @SerialName("previewUrl") val previewUrl: String? = null,
    @SerialName("primaryGenreName") val primaryGenreName: String? = null,
    @SerialName("trackTimeMillis") val trackTimeMillis: Long? = null,
    @SerialName("releaseDate") val releaseDate: String? = null,
    @SerialName("trackCount") val trackCount: Int? = null,
    @SerialName("wrapperType") val wrapperType: String? = null,
)
