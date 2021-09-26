package com.igorleite.themooviebd.domain.remote

import com.igorleite.themooviebd.data.RequestState
import com.igorleite.themooviebd.data.getResponse
import com.igorleite.themooviebd.data.model.Movie
import com.igorleite.themooviebd.data.remote.repository.MoviesRepository
import com.igorleite.themooviebd.domain.BaseUseCase
import com.igorleite.themooviebd.utils.modelToListMovies
import javax.inject.Inject

class GetMoviesByName @Inject constructor(private val moviesRepository: MoviesRepository) :
    BaseUseCase<RequestState<List<Movie>>, GetMoviesByName.Params> {
    override suspend fun run(params: Params): RequestState<List<Movie>> {
        return when (val response =
            moviesRepository.getMoviesByName(params.type, params.search, params.page.toString())
                .getResponse()) {
            is RequestState.ResponseSuccess -> {
                response.value?.let {
                    RequestState.ResponseSuccess(it.movieList.modelToListMovies().sortedBy { _movie ->
                        _movie.imdbID
                    })
                } ?: RequestState.ResponseException(Exception("Body is null"))
            }
            is RequestState.ResponseFailure -> {
                RequestState.ResponseFailure(response.error)
            }
            is RequestState.ResponseException -> {
                RequestState.ResponseException(response.exception)
            }
            else -> {
                RequestState.ResponseException(Exception(""))
            }
        }
    }

    data class Params(
        val type: String,
        val search: String,
        val page: Int
    )
}