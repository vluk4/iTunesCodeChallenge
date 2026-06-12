package com.vluk4.itunescodechallenge.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vluk4.itunescodechallenge.core.database.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {

    @Upsert
    suspend fun upsert(key: RemoteKeyEntity)

    @Query("SELECT * FROM remote_keys WHERE `query` = :query LIMIT 1")
    suspend fun remoteKey(query: String): RemoteKeyEntity?

    @Query("DELETE FROM remote_keys WHERE `query` = :query")
    suspend fun clearByQuery(query: String)
}
