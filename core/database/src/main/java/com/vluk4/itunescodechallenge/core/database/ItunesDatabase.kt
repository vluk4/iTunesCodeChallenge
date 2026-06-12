package com.vluk4.itunescodechallenge.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vluk4.itunescodechallenge.core.database.dao.RemoteKeyDao
import com.vluk4.itunescodechallenge.core.database.dao.SongDao
import com.vluk4.itunescodechallenge.core.database.entity.RemoteKeyEntity
import com.vluk4.itunescodechallenge.core.database.entity.SongEntity

@Database(
    entities = [SongEntity::class, RemoteKeyEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class ItunesDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}
