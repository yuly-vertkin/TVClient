package com.example.mytest.data

import com.example.mytest.domain.ChannelCategory

sealed class Response<out T> {
    object Loading: Response<Nothing>()
    data class Success<out T>(val data: T): Response<T>()
    data class Error(val error: Throwable): Response<Nothing>()
}

interface DataSource {
    suspend fun getChannelCategoryList(): Response<List<ChannelCategory>>
}