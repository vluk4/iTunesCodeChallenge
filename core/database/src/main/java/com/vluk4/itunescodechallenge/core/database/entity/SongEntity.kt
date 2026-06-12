package com.vluk4.itunescodechallenge.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val artistName: String,
    val collectionId: Long,
    val collectionName: String,
    val artworkUrl: String,
    val previewUrl: String?,
    val genre: String?,
    val trackTimeMillis: Long?,
    val releaseDate: String?,
    val searchQuery: String?,
    val pageIndex: Int?,
    val lastPlayedAt: Long?,
)
