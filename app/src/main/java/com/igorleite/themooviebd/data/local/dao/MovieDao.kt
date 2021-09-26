package com.igorleite.themooviebd.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.igorleite.themooviebd.data.model.entities.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movie")
    fun getAllMovies(): PagingSource<Int, MovieEntity>

    @Query("DELETE FROM movie WHERE favorite = 0")
    suspend fun deleteAllMovies()
}