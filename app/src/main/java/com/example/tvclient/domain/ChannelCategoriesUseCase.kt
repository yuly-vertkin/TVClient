package com.example.tvclient.domain

import com.example.tvclient.data.ChannelCategoryRepository
import com.example.tvclient.data.Response
import com.example.tvclient.data.UserPreferences
import com.example.tvclient.data.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ChannelCategoriesUseCase @Inject constructor(
    private val repository: ChannelCategoryRepository,
    private val userPrefRepository: UserPreferencesRepository
) {
    fun getChannelCategoryList(): Flow<Response<List<ChannelCategory>>> =
        combine(
            repository.getData(),
            userPrefRepository.userPreferences
        ) { itemsRes: Response<List<ChannelCategory>>, userPreferences: UserPreferences ->
            if (itemsRes is Response.Success) {
                Response.Success(itemsRes.data.take(userPreferences.maxItems))
            } else {
                itemsRes
            }
        }

    suspend fun updateData() {
        repository.updateData()
    }

    fun getUserPreferences(): Flow<UserPreferences> =
        userPrefRepository.userPreferences

    suspend fun updateIsLoggedIn(isLoggedIn: Boolean) =
        userPrefRepository.updateIsLoggedIn(isLoggedIn)

    suspend fun updateMaxItems(maxItems: Int) =
        userPrefRepository.updateMaxItems(maxItems)
}