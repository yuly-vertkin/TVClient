package com.example.tvclient.presentation

import androidx.lifecycle.*
import com.example.tvclient.data.Response
import com.example.tvclient.domain.ChannelCategoriesUseCase
import com.example.tvclient.domain.ChannelCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TVClientViewModel @Inject constructor(
    private val channelCategoriesUseCase: ChannelCategoriesUseCase
    ) : ViewModel() {

    private val channelCategoryList: LiveData<Response<List<ChannelCategory>>> =
        channelCategoriesUseCase.getChannelCategoryList().asLiveData()

    val items: LiveData<List<ChannelCategory>> = channelCategoryList.map {
        when(it) {
            is Response.Success -> it.data
            else -> emptyList()
        }
    }

    val isLoading: LiveData<Boolean> = channelCategoryList.map { it is Response.Loading }

    fun updateMaxItems(maxItems: Int) = viewModelScope.launch {
        channelCategoriesUseCase.updateMaxItems(maxItems)
    }
}

