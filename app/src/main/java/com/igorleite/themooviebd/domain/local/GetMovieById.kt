package com.igorleite.themooviebd.domain.local

import com.igorleite.themooviebd.data.RequestState
import com.igorleite.themooviebd.data.local.repository.MoviesLocalRepository
import com.igorleite.themooviebd.data.model.Movie
import com.igorleite.themooviebd.domain.BaseUseCase
import com.igorleite.themooviebd.utils.toDomain
import javax.inject.Inject

class GetMovieById @Inject constructor(
    private val localRepository: MoviesLocalRepository,
) :
    BaseUseCase<RequestState<Movie>, GetMovieById.Params> {

    override suspend fun run(params: Params): RequestState<Movie> {
        return try {
            RequestState.ResponseSuccess(localRepository.getMovieById(params.movie.imdbID).toDomain())
        } catch (e: Exception) {
            RequestState.ResponseException(e)
        }
    }

    data class Params(
        val movie: Movie
    )
}