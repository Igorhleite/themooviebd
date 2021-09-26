package com.igorleite.themooviebd.di

import com.igorleite.themooviebd.data.local.dao.FavoriteDao
import com.igorleite.themooviebd.data.local.repository.MoviesLocalRepository
import com.igorleite.themooviebd.data.local.repository.MoviesLocalRepositoryImpl
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
object RepositoryModule {
    @Singleton
    @Provides
    fun provideRemoteMoviesRepository(
        apiClient: ApiClient
    ): MoviesRepository {
        return MoviesRepositoryImpl(apiClient)
    }

    @Singleton
    @Provides
    fun provideLocalMoviesRepository(
        favoriteDao: FavoriteDao
    ): MoviesLocalRepository {
        return MoviesLocalRepositoryImpl(favoriteDao)
    }
}