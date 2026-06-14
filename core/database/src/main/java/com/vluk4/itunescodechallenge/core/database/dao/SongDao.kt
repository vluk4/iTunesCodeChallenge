package com.vluk4.itunescodechallenge.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.vluk4.itunescodechallenge.core.database.entity.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

    @Query(
        "SELECT * FROM songs WHERE searchQuery = :query ORDER BY sortIndex ASC, id ASC"
    )
    fun pagingSource(query: String): PagingSource<Int, SongEntity>

    @Transaction
    suspend fun cacheSongs(songs: List<SongEntity>) {
        songs.forEach { incoming ->
            val existing = getById(incoming.id)
            val merged = if (existing == null) {
                incoming
            } else {
                incoming.copy(
                    searchQuery = incoming.searchQuery ?: existing.searchQuery,
                    sortIndex = incoming.sortIndex ?: existing.sortIndex,
                    lastPlayedAt = incoming.lastPlayedAt ?: existing.lastPlayedAt,
                )
            }
            upsert(merged)
        }
    }

    @Query("DELETE FROM songs WHERE searchQuery = :query AND lastPlayedAt IS NULL")
    suspend fun clearByQuery(query: String)

    @Query(
        "DELETE FROM songs WHERE searchQuery IS NOT NULL AND searchQuery != :activeQuery AND lastPlayedAt IS NULL"
    )
    suspend fun clearInactiveSearchCaches(activeQuery: String)

    @Query("SELECT * FROM songs WHERE lastPlayedAt IS NOT NULL ORDER BY lastPlayedAt DESC LIMIT :limit")
    fun observeRecentlyPlayed(limit: Int): Flow<List<SongEntity>>

    @Upsert
    suspend fun upsert(song: SongEntity)

    @Query("SELECT * FROM songs WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): SongEntity?
}
