package com.example.tvclient.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

data class UserPreferences(val isLoggedIn: Boolean, val maxItems: Int)

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
    ) {
    private object PreferencesKeys {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val MAX_ITEMS = intPreferencesKey("max_items")
    }

    val userPreferences: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val isLoggedIn = preferences[PreferencesKeys.IS_LOGGED_IN] ?: false
            val maxItems = preferences[PreferencesKeys.MAX_ITEMS] ?: Int.MAX_VALUE
            UserPreferences(isLoggedIn, maxItems)
        }

    suspend fun updateIsLoggedIn(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] = isLoggedIn
        }
    }

    suspend fun updateMaxItems(maxItems: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MAX_ITEMS] = maxItems
        }
    }
}