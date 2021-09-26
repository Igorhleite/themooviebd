package com.igorleite.themooviebd.data.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class MovieEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var imdbID: String,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "poster")
    var poster: String,
    @ColumnInfo(name = "favorite")
    var favorite: Boolean
)