package com.igorleite.themooviebd.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.igorleite.themooviebd.data.local.MovieDataBase
import com.igorleite.themooviebd.data.model.Movie
import com.igorleite.themooviebd.domain.local.DeleteMovie
import com.igorleite.themooviebd.domain.local.SaveMovie
import com.igorleite.themooviebd.domain.remote.GetMoviesByName
import com.igorleite.themooviebd.ui.adapter.paging.MoviePaging
import com.igorleite.themooviebd.ui.adapter.paging.mediator.MovieRemoteMediator
import com.igorleite.themooviebd.utils.Constants
import com.igorleite.themooviebd.utils.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagingApi
class HomeViewModel
@Inject constructor(
    private val getMoviesByName: GetMoviesByName,
    private val saveMovie: SaveMovie,
    private val deleteMovie: DeleteMovie,
    private val movieDataBase: MovieDataBase,
) : ViewModel() {

    private val _movieList = MutableLiveData<PagingData<Movie>>()
    val movieList get() = _movieList


    init {
        getMoviesFromMediator("", "iron man")
    }

    /** This function implements paging 3
     * Its works without mediator.
     * performs paging, but without local cache
     ** @param type specific type of search "movie, series, episode"
     ** @param search title for search in api
     ** @sample getMoviesFromPagingWithoutMediator("movie","marvel")
     * @author Igorhleite
     **/
    fun getMoviesFromPagingWithoutMediator(type: String, search: String) {
        viewModelScope.launch {
            Pager(PagingConfig(pageSize = Constants.PAGE_SIZE)) {
                MoviePaging(getMoviesByName, type, search)
            }.flow.cachedIn(viewModelScope).collect {
                _movieList.value = it
            }
        }
    }

    /** This function implements paging 3
     * Its works with mediator.
     * performs paging, with local cache.
     ** @param type specific type of search "movie, series, episode"
     ** @param search title for search in api
     ** @sample getMoviesFromPagingWithoutMediator("movie","marvel")
     * @author Igorhleite
     **/
    private fun getMoviesFromMediator(type: String, search: String) {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    pageSize = Constants.PAGE_SIZE,
                    enablePlaceholders = false,
                ),
                remoteMediator = MovieRemoteMediator(
                    useCase = getMoviesByName,
                    database = movieDataBase,
                    type = type,
                    search = search
                ),
                pagingSourceFactory = { movieDataBase.movieDao().getAllMovies() }
            ).flow.cachedIn(viewModelScope).collect {
                _movieList.value = it.map { entity ->
                    entity.toDomain()
                }
            }
        }
    }

    fun setMovieFavorite(movie: Movie) {
        viewModelScope.launch {
            saveMovie.run(SaveMovie.Params(movie))
        }
    }

    fun removeMovieFavorite(movie: Movie) {
        viewModelScope.launch {
            deleteMovie.run(DeleteMovie.Params(movie))
        }
    }
}