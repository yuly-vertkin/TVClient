package com.example.tvclient.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit

abstract class Repository<T> {
    private var cache: T? = null
    protected abstract val localData: () -> Flow<T>
    protected abstract val remoteData: suspend () -> Response<T>

    protected abstract suspend fun updateLocalData(data: T)

    private var updateTime = 0L

    fun getData() : Flow<Response<T>> = flow {
            emit(Response.Loading)
//            delay(3000)

            val curTime = System.currentTimeMillis()
            if (curTime - updateTime > FRESH_TIMEOUT) {
                val res = remoteData()
                if (res is Response.Success) {
                    updateLocalData(res.data)
                    updateTime = curTime
                }
            }
            localData().collect {
                emit(Response.Success(it))
            }
        }

    companion object {
        private val FRESH_TIMEOUT = TimeUnit.HOURS.toMillis(1)  // an hour
    }
}