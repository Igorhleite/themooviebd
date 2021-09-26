package com.igorleite.themooviebd.di

import android.content.Context
import androidx.room.Room
import com.igorleite.themooviebd.data.local.MovieDataBase
import com.igorleite.themooviebd.data.local.dao.FavoriteDao
import com.igorleite.themooviebd.data.local.dao.MovieDao
import com.igorleite.themooviebd.data.local.dao.RemoteKeyDao
import com.igorleite.themooviebd.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideMovieDataBase(
        @ApplicationContext context: Context
    ): MovieDataBase {
        return Room.databaseBuilder(
            context, MovieDataBase::class.java,
            Constants.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideFavoriteDao(
        appDatabase: MovieDataBase
    ): FavoriteDao {
        return appDatabase.favoriteDao()
    }
}