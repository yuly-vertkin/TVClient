package com.example.tvclient.domain

import com.example.tvclient.data.ChannelCategoryRepository
import com.example.tvclient.data.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChannelCategoriesUseCase @Inject constructor(
    private val repository: ChannelCategoryRepository
) {
    fun getChannelCategoryList(): Flow<Response<List<ChannelCategory>>> =
        repository.getData()
}