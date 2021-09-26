package com.igorleite.themooviebd.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.igorleite.themooviebd.data.local.dao.FavoriteDao
import com.igorleite.themooviebd.data.local.dao.MovieDao
import com.igorleite.themooviebd.data.local.dao.RemoteKeyDao
import com.igorleite.themooviebd.data.model.entities.FavoriteEntity
import com.igorleite.themooviebd.data.model.entities.KeysEntity
import com.igorleite.themooviebd.data.model.entities.MovieEntity

@Database(version = 1, entities = [FavoriteEntity::class, MovieEntity::class, KeysEntity::class])
abstract class MovieDataBase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun remoteKeysDao(): RemoteKeyDao
    abstract fun favoriteDao(): FavoriteDao
}