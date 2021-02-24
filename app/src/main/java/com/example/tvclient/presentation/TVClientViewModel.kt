package com.example.mytest.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.mytest.data.Response
import com.example.mytest.domain.ChannelCategoriesUseCase
import com.example.mytest.domain.ChannelCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class TVClientViewModel @Inject constructor(private val channelCategoriesUseCase: ChannelCategoriesUseCase) : ViewModel() {

    val channelCategoryList: LiveData<Response<List<ChannelCategory>>> = liveData {
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

