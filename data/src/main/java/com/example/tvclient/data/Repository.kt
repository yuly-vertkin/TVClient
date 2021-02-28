package com.example.tvclient.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

abstract class Repository<T> {
    private var cache: T? = null
    protected abstract val remoteData: suspend () -> Response<T>

//    suspend fun getData() : Response<T> {
//        if (cache != null)  {
//            return Response.Success(cache!!)
//        }
//        val res = remoteData()
//        if (res is Response.Success) {
//            cache = res.data
//        }
//        return res
//    }

    suspend fun getData() : Flow<Response<T>> = flow {
        if (cache != null) {
            emit(Response.Success(cache!!))
        } else {
            emit(Response.Loading)

            val res = remoteData ()
            if (res is Response.Success) {
                cache = res.data
            }
            emit(res)
        }
    }.catch {
        emit(Response.Error(it))
    }
}