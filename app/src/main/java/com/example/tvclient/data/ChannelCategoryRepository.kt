package com.example.tvclient.data

import com.example.tvclient.data.database.ChannelCategoryDao
import com.example.tvclient.data.database.ChannelCategoryEntity
import com.example.tvclient.domain.ChannelCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelCategoryRepository @Inject constructor(
    private val dataSource: RemoteDataSource,
    private val channelCategoryDao: ChannelCategoryDao
    ) : Repository<List<ChannelCategory>>() {

    override val localData: () -> Flow<List<ChannelCategory>>
        get() = { channelCategoryDao.getAll().map { it.convertFrom() }}

    override suspend fun updateLocalData(data: List<ChannelCategory>) {
        channelCategoryDao.insertAll(data.convertTo())
    }

    override val remoteData: suspend () -> Response<List<ChannelCategory>>
        get() = { dataSource.getChannelCategoryList() }

}

fun List<ChannelCategoryEntity>.convertFrom() : List<ChannelCategory> {
    return this.map {
        ChannelCategory(it.categoryId, it.categoryName!!)
    }
}

fun List<ChannelCategory>.convertTo() : List<ChannelCategoryEntity> {
    return this.map {
        ChannelCategoryEntity(categoryId = it.id, categoryName = it.name)
    }
}