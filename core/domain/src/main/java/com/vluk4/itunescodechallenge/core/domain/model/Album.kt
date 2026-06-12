package com.vluk4.itunescodechallenge.core.domain.model

data class Album(
    val id: Long,
    val name: String,
    val artistName: String,
    val artworkUrl: String,
    val genre: String?,
    val releaseDate: String?,
    val trackCount: Int,
    val tracks: List<Song>,
)
