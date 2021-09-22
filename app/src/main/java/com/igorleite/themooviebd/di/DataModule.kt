package com.igorleite.themooviebd.di

import com.igorleite.themooviebd.data.remote.ApiClient
import com.igorleite.themooviebd.data.remote.repository.MoviesRepository
import com.igorleite.themooviebd.data.remote.repository.MoviesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideMoviesRepository(
        apiClient: ApiClient
    ): MoviesRepository {
        return MoviesRepositoryImpl(apiClient)
    }
}