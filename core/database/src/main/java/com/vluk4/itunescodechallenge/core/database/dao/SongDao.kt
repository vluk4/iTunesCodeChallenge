package com.vluk4.itunescodechallenge.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.vluk4.itunescodechallenge.core.database.entity.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

    @Query(
        "SELECT * FROM songs WHERE searchQuery = :query ORDER BY pageIndex ASC, id ASC"
    )
    fun pagingSource(query: String): PagingSource<Int, SongEntity>

    @Upsert
    suspend fun upsertAll(songs: List<SongEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(songs: List<SongEntity>)

    @Query("DELETE FROM songs WHERE searchQuery = :query")
    suspend fun clearByQuery(query: String)

    @Query("SELECT * FROM songs WHERE lastPlayedAt IS NOT NULL ORDER BY lastPlayedAt DESC LIMIT :limit")
    fun observeRecentlyPlayed(limit: Int): Flow<List<SongEntity>>

    @Upsert
    suspend fun upsert(song: SongEntity)

    @Query("SELECT * FROM songs WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): SongEntity?
}
