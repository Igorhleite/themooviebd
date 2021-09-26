package com.igorleite.themooviebd.domain.local

import com.igorleite.themooviebd.data.RequestState
import com.igorleite.themooviebd.data.local.repository.MoviesLocalRepository
import com.igorleite.themooviebd.data.model.Movie
import com.igorleite.themooviebd.domain.BaseUseCase
import com.igorleite.themooviebd.domain.local.GetAllMovies.Params
import javax.inject.Inject

class GetAllMovies @Inject constructor(
    private val localRepository: MoviesLocalRepository,
) :
    BaseUseCase<RequestState<List<Movie>?>, Params> {

    override suspend fun run(params: Params): RequestState<List<Movie>?> {
        return try {
            RequestState.ResponseException(java.lang.Exception(""))
        } catch (e: Exception) {
            RequestState.ResponseException(e)
        }
    }

    data class Params(
        val movie: Movie
    )
}