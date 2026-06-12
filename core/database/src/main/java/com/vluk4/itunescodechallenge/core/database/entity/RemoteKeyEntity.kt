package com.vluk4.itunescodechallenge.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val query: String,
    val nextOffset: Int?,
)
