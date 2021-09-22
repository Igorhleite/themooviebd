package com.igorleite.themooviebd.ui

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.igorleite.themooviebd.data.State
import com.igorleite.themooviebd.data.model.MovieModel
import com.igorleite.themooviebd.domain.GetMoviesByName

class MoviePaging(
    private val getMoviesByName: GetMoviesByName,
    private val type: String,
    private val search: String
) : PagingSource<Int, MovieModel>() {


    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {
        val page = params.key ?: 1
        return try {
            when (val response =
                getMoviesByName.run(GetMoviesByName.Params(type, search, page.toString()))) {
                is State.ResponseSuccess -> {
                    val list = response.value?.movieList ?: emptyList()
                    LoadResult.Page(
                        data = list,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (list.isEmpty()) null else page + 1
                    )
                }
                is State.ResponseFailure -> {
                    LoadResult.Error(Exception(response.error.toString()))
                }
                else -> LoadResult.Error(Exception("Unxpected Exception"))
            }
        } catch (e: Exception) {
            LoadResult.Error(Exception("Sorry, there was an error ;("))
        }
    }
}