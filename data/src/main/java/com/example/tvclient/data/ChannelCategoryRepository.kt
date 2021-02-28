package com.example.tvclient.data

import com.example.tvclient.domain.ChannelCategory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelCategoryRepository @Inject constructor(private val dataSource: DataSource) : Repository<List<ChannelCategory>>() {

    override val remoteData: suspend () -> Response<List<ChannelCategory>>
        get() = { dataSource.getChannelCategoryList() }

//    suspend fun getChannelCategoryList(): Response<List<ChannelCategory>> = dataSource.getChannelCategoryList()
}
