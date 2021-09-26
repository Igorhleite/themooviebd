package com.igorleite.themooviebd.ui.adapter.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.igorleite.themooviebd.data.RequestState
import com.igorleite.themooviebd.data.model.Movie
import com.igorleite.themooviebd.domain.remote.GetMoviesByName

/** This class implements paging 3
 * Its works without mediator.
 * performs paging, but without local cache
 ** @param getMoviesByName useCase for call your remote repository
 ** @param type specific type of search "movie, series, episode"
 ** @param search title for search in api
 ** @sample getMoviesList("movie","marvel")
 * @author Igorhleite
 **/
class MoviePaging(
    private val getMoviesByName: GetMoviesByName,
    private val type: String,
    private val search: String
) : PagingSource<Int, Movie>() {


    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            when (val response =
                getMoviesByName.run(GetMoviesByName.Params(type, search, page))) {
                is RequestState.ResponseSuccess -> {
                    val list = response.value
                    LoadResult.Page(
                        data = list,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (list.isEmpty()) null else page + 1
                    )
                }
                is RequestState.ResponseFailure -> {
                    LoadResult.Error(Exception(response.error.toString()))
                }
                else -> LoadResult.Error(Exception("Unxpected Exception"))
            }
        } catch (e: Exception) {
            LoadResult.Error(Exception("Sorry, there was an error ;("))
        }
    }
}