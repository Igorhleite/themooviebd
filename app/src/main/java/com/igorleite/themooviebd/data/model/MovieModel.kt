package com.igorleite.themooviebd.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieModel(
    @SerializedName("Title")
    var title: String,
    @SerializedName("Poster")
    var poster: String,
    var favorite: Boolean,
    var imdbID: String,
) : Parcelable