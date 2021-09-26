package com.igorleite.themooviebd.data.local.repository

import com.igorleite.themooviebd.data.model.entities.FavoriteEntity

interface MoviesLocalRepository {
    suspend fun saveLocalMovie(movie: FavoriteEntity): Long

    suspend fun deleteLocalMovie(movie: FavoriteEntity): Int

    suspend fun getMovieById(id: String): FavoriteEntity
}