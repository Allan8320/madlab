package com.example.exp7

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// One DataStore file named "settings" for the whole app
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object SortPreferences {
    // true  = "Newest First"
    // false = "Title A-Z"  (default)
    private val NEWEST_FIRST = booleanPreferencesKey("newest_first")

    fun newestFirstFlow(context: Context): Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[NEWEST_FIRST] ?: false }

    suspend fun setNewestFirst(context: Context, value: Boolean) {
        context.dataStore.edit { prefs -> prefs[NEWEST_FIRST] = value }
    }
}
