package com.igorleite.themooviebd.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.igorleite.themooviebd.data.model.entities.KeysEntity

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllKeys(keyEntities: List<KeysEntity>)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun getRemoteKesById(id: String): KeysEntity?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAllKeys()
}