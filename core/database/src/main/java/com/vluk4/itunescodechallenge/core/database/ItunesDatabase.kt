package com.vluk4.itunescodechallenge.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vluk4.itunescodechallenge.core.database.dao.SongDao
import com.vluk4.itunescodechallenge.core.database.entity.SongEntity

// v2: dropped the remote_keys table — the iTunes API can't paginate, so search
// results are fetched in one page and paged from the cache locally.
@Database(
    entities = [SongEntity::class],
    version = 2,
    exportSchema = true,
)
abstract class ItunesDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
}
