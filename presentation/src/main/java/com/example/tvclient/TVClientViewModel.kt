package com.example.tvclient

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.tvclient.data.Response
import com.example.tvclient.domain.ChannelCategoriesUseCase
import com.example.tvclient.domain.ChannelCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TVClientViewModel @Inject constructor(private val channelCategoriesUseCase: com.example.tvclient.domain.ChannelCategoriesUseCase) : ViewModel() {

    val channelCategoryList: LiveData<com.example.tvclient.data.Response<List<ChannelCategory>>> = liveData {
        channelCategoriesUseCase.getChannelCategoryList().collect {
            emit(it)
        }
    }


//    val channelCategoryList: LiveData<Response<List<ChannelCategory>>> = liveData {
//        val res = channelCategoriesUseCase.getChannelCategoryList()
//        emit(res)
//    }

//    val channelCategoryList: LiveData<Response<List<ChannelCategory>>> = liveData {
//        val res = ChannelCategoriesUseCase(Repository(MwDataSource(MwApi()))).getChannelCategoryList()
//        emit(res)
//    }
}

