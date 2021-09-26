package com.igorleite.themooviebd.data.remote.repository

import com.igorleite.themooviebd.data.model.SearchModel
import retrofit2.Response

interface MoviesRepository {
    suspend fun getMoviesByName(type: String, search: String, page: String): Response<SearchModel>
}
