package com.example.tvclient.presentation

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.SpannedString
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvDetailViewModeL @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val name: LiveData<SpannedString> = savedStateHandle.getLiveData<String>(NAME).map {
        buildSpannedString {
            makeText(it)
        }
    }

    private fun SpannableStringBuilder.makeText(text: String) =
        color(Color.RED) {
            bold {
                append(text)
            }
        }

    companion object {
        const val NAME = "name"
    }
}