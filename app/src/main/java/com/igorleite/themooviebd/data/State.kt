package com.igorleite.themooviebd.data

import okhttp3.ResponseBody
import retrofit2.Response


sealed class State<out T> {
    data class ResponseSuccess<out R>(val value: R) : State<R>()
    data class ResponseFailure(val error: ResponseBody?) : State<Nothing>()
    data class ResponseException(val exception: Exception) : State<Nothing>()
}

fun <R> Response<R>.getResponse(): State<R?> {
    return try {
        when (this.isSuccessful) {
            true -> {
                State.ResponseSuccess(body())
            }
            false -> {
                State.ResponseFailure(errorBody())
            }
        }
    } catch (e: Exception) {
        State.ResponseException(e)
    }
}