package com.igorleite.themooviebd.data.local.repository

import com.igorleite.themooviebd.data.local.dao.FavoriteDao
import com.igorleite.themooviebd.data.model.entities.FavoriteEntity
import javax.inject.Inject

class MoviesLocalRepositoryImpl
@Inject
constructor(
    private val favoriteDao: FavoriteDao
) : MoviesLocalRepository {
    override suspend fun saveLocalMovie(movie: FavoriteEntity): Long {
        return favoriteDao.setFavorite(movie)
    }

    override suspend fun deleteLocalMovie(movie: FavoriteEntity): Int {
        return favoriteDao.deleteFavorite(movie.imdbID)
    }

    override suspend fun getMovieById(id: String): FavoriteEntity {
        return favoriteDao.getFavorite(id)
    }
}