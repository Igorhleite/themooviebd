package com.igorleite.themooviebd.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.igorleite.themooviebd.data.model.entities.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setFavorite(favoriteMovie: FavoriteEntity): Long

    @Query("DELETE FROM favorite_movie WHERE id = :id")
    suspend fun deleteFavorite(id: String): Int

    @Query("SELECT * FROM favorite_movie")
    suspend fun getFavorites(): List<FavoriteEntity>

    @Query("SELECT * FROM favorite_movie WHERE id = :id")
    suspend fun getFavorite(id: String): FavoriteEntity
}