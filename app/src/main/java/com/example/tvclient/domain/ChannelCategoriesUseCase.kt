package com.example.mytest.domain

import com.example.mytest.data.ChannelCategoryRepository
import com.example.mytest.data.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChannelCategoriesUseCase @Inject constructor(val repository: ChannelCategoryRepository) {
    suspend fun getChannelCategoryList(): Flow<Response<List<ChannelCategory>>> = repository.getData()
}