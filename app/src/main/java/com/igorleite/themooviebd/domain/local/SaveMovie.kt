package com.igorleite.themooviebd.domain.local

import com.igorleite.themooviebd.data.RequestState
import com.igorleite.themooviebd.data.local.repository.MoviesLocalRepository
import com.igorleite.themooviebd.data.model.Movie
import com.igorleite.themooviebd.domain.BaseUseCase
import com.igorleite.themooviebd.domain.local.SaveMovie.Params
import com.igorleite.themooviebd.utils.toFavorite
import javax.inject.Inject

class SaveMovie @Inject constructor(
    private val localRepository: MoviesLocalRepository,
) :
    BaseUseCase<RequestState<Boolean?>, Params> {

    override suspend fun run(params: Params): RequestState<Boolean?> {
        return try {
            RequestState.ResponseSuccess(localRepository.saveLocalMovie(params.movie.toFavorite()) != -1L)
        } catch (e: Exception) {
            RequestState.ResponseException(e)
        }
    }

    data class Params(
        val movie: Movie
    )
}