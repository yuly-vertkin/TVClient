package com.example.tvclient.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvDetailViewModeL @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val name: LiveData<String> = savedStateHandle.getLiveData<String>(NAME)

    companion object {
        const val NAME = "name"
    }
}