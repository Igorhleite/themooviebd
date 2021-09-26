package com.igorleite.themooviebd.data.remote

import com.igorleite.themooviebd.BuildConfig
import com.igorleite.themooviebd.data.model.dto.SearchModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {
    @GET(".")
    suspend fun searchByName(
        @Query(value = "type") type: String,
        @Query(value = "s") search: String,
        @Query(value = "page") page: String,
        @Query(value = "apikey") apiKey: String = BuildConfig.API_KEY
    ): Response<SearchModel>
}