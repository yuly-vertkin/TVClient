package com.example.mytest.data.mvapi

import com.example.mytest.data.DataSource
import com.example.mytest.data.Response
import com.example.mytest.domain.ChannelCategory
import javax.inject.Inject

class MwDataSource @Inject constructor(private val mwApi: MwApi) : DataSource {
    override suspend fun getChannelCategoryList() = mwApi.getChannelCategoryList().convert {
        map {
            ChannelCategory(it.id, it.name)
        }
    }

    private fun <OUT, IN> Response<IN>.convert(mapper: IN.() -> OUT): Response<OUT> {
        return when (this) {
            is Response.Loading -> Response.Loading
            is Response.Success -> Response.Success(mapper(data))
            is Response.Error -> Response.Error(error)
        }
    }
}