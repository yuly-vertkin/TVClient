package com.example.tvclient.presentation

import android.view.View
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.example.tvclient.data.Response
import com.example.tvclient.domain.ChannelCategoriesUseCase
import com.example.tvclient.domain.ChannelCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TVFragmentViewModel @Inject constructor(
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

