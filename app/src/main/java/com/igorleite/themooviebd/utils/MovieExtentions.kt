package com.igorleite.themooviebd.utils

import com.igorleite.themooviebd.data.model.Movie
import com.igorleite.themooviebd.data.model.dto.MovieModel
import com.igorleite.themooviebd.data.model.entities.FavoriteEntity
import com.igorleite.themooviebd.data.model.entities.MovieEntity

fun MovieModel.toDomain(): Movie {
    return Movie(
        imdbID = this.imdbID,
        title = this.title,
        poster = this.poster,
        favorite = this.favorite
    )
}

fun MovieEntity.toDomain(): Movie {
    return Movie(
        imdbID = this.imdbID,
        title = this.title,
        poster = this.poster,
        favorite = this.favorite
    )
}

fun Movie.toFavorite(): FavoriteEntity {
    return FavoriteEntity(
        imdbID = this.imdbID,
        title = this.title,
        poster = this.poster,
        favorite = this.favorite
    )
}

fun FavoriteEntity.toDomain(): Movie {
    return Movie(
        imdbID = this.imdbID,
        title = this.title,
        poster = this.poster,
        favorite = this.favorite
    )
}

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        imdbID = this.imdbID,
        title = this.title,
        poster = this.poster,
        favorite = this.favorite
    )
}

fun List<MovieModel>.modelToListMovies(): List<Movie> {
    return this.map { it.toDomain() }
}

fun List<MovieEntity>.entityToListMovies(): List<Movie> {
    return this.map { it.toDomain() }
}

fun List<Movie>.fromDomainsToEntities(): List<MovieEntity> {
    return this.map { it.toEntity() }
}
