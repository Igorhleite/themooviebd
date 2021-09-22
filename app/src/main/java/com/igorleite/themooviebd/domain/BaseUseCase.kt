package com.igorleite.themooviebd.domain

interface BaseUseCase<out Type, in Params> {
    suspend fun run(params: Params): Type
}

