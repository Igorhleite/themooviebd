package com.igorleite.themooviebd.data.remote.repository

import com.igorleite.themooviebd.data.model.dto.SearchModel
import com.igorleite.themooviebd.data.remote.ApiClient
import retrofit2.Response
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient
) : MoviesRepository {
    override suspend fun getMoviesByName(
        type: String,
        search: String,
        page: String
    ): Response<SearchModel> {
        println("<> ${page.toString()}")
        return apiClient.searchByName(type, search, page)
    }
}