package com.example.tvclient.presentation

import android.view.View
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.example.tvclient.data.Response
import com.example.tvclient.domain.ChannelCategoriesUseCase
import com.example.tvclient.domain.ChannelCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class TVFragmentViewModel @Inject constructor(
    private val channelCategoriesUseCase: ChannelCategoriesUseCase
    ) : ViewModel() {

    private val channelCategoryList: Flow<Response<List<ChannelCategory>>> =
        channelCategoriesUseCase.getChannelCategoryList()

    val items: StateFlow<List<ChannelCategory>> = channelCategoryList.map {
        when(it) {
            is Response.Success -> it.data
            else -> emptyList()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val isLoading: StateFlow<Boolean> = channelCategoryList.map {
        it is Response.Loading
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

// with LiveData
//    private val channelCategoryList: LiveData<Response<List<ChannelCategory>>> =
//        channelCategoriesUseCase.getChannelCategoryList().asLiveData()
//
//    val items: LiveData<List<ChannelCategory>> = channelCategoryList.map {
//        when(it) {
//            is Response.Success -> it.data
//            else -> emptyList()
//        }
//    }
//
//    val isLoading: LiveData<Boolean> = channelCategoryList.map { it is Response.Loading }

    fun onClick(view: View, name: String) {
        val action = TVFragmentDirections.nextAction(name)
        view.findNavController().navigate(action)
// without safe args
//        val bundle = bundleOf(TvDetailViewModeL.NAME to name)
//        view.findNavController().navigate(R.id.action_TVFragment_to_TVDetailFragment, bundle)
    }

    fun updateMaxItems(maxItems: Int) = viewModelScope.launch {
        channelCategoriesUseCase.updateMaxItems(maxItems)
    }

    suspend fun update() {
        channelCategoriesUseCase.updateData()
    }
}

