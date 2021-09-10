package com.example.tvclient.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

data class UserPreferences(val maxItems: Int)

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
    ) {
    private object PreferencesKeys {
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
            val maxItems = preferences[PreferencesKeys.MAX_ITEMS] ?: Int.MAX_VALUE
            UserPreferences(maxItems)
        }

    suspend fun updateMaxItems(maxItems: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MAX_ITEMS] = maxItems
        }
    }
}