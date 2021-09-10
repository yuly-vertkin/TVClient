package com.example.tvclient

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp

private const val USER_PREFERENCES_NAME = "user_preferences"

@HiltAndroidApp
class TVClientApplication : Application() {

    val dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )
}