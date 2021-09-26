package com.igorleite.themooviebd.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    var title: String,
    var poster: String,
    var favorite: Boolean,
    var imdbID: String,
) : Parcelable