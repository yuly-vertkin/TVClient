package com.example.tvclient.domain

import com.example.tvclient.data.ChannelCategoryRepository
import com.example.tvclient.data.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChannelCategoriesUseCase @Inject constructor(val repository: com.example.tvclient.data.ChannelCategoryRepository) {
    suspend fun getChannelCategoryList(): kotlinx.coroutines.flow.Flow<com.example.tvclient.data.Response<List<ChannelCategory>>> = repository.getData()
}