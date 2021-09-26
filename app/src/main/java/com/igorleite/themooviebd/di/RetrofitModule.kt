package com.igorleite.themooviebd.di

import com.igorleite.themooviebd.data.remote.ApiClient
import com.igorleite.themooviebd.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideMovieApi(
        retrofit: Retrofit.Builder
    ): ApiClient {
        return retrofit
            .build()
            .create(ApiClient::class.java)
    }
}