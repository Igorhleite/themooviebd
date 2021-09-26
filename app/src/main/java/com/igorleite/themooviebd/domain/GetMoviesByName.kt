package com.igorleite.themooviebd.domain

import com.igorleite.themooviebd.data.State
import com.igorleite.themooviebd.data.getResponse
import com.igorleite.themooviebd.data.model.SearchModel
import com.igorleite.themooviebd.data.remote.repository.MoviesRepository
import javax.inject.Inject

class GetMoviesByName @Inject constructor(private val moviesRepository: MoviesRepository) :
    BaseUseCase<State<SearchModel?>, GetMoviesByName.Params> {
    override suspend fun run(params: Params): State<SearchModel?> {
        return when (val response =
            moviesRepository.getMoviesByName(params.type, params.search, params.page)
                .getResponse()) {
            is State.ResponseSuccess -> {
                response.value.let {
                    State.ResponseSuccess(it)
                }
            }
            is State.ResponseFailure -> {
                State.ResponseFailure(response.error)
            }
            is State.ResponseException -> {
                State.ResponseException(response.exception)
            }
        }
    }

    data class Params(
        val type: String,
        val search: String,
        val page: String
    )
}