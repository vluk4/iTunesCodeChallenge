package com.vluk4.itunescodechallenge.core.domain.model

data class Song(
    val id: Long,
    val title: String,
    val artistName: String,
    val collectionId: Long,
    val collectionName: String,
    val artworkUrl: String,
    val previewUrl: String?,
    val genre: String?,
    val trackTimeMillis: Long?,
    val releaseDate: String?,
)
