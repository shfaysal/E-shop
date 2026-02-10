package com.example.e_shop.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private val IS_DARK_MODE_KEY = booleanPreferencesKey("is_dark_mode")
    private val LANGUAGE_KEY = stringPreferencesKey("app_language")

    val isDarkMode: Flow<Boolean?> = context.settingsDataStore.data
        .map { preferences ->
            preferences[IS_DARK_MODE_KEY]
        }
        
    val language: Flow<String> = context.settingsDataStore.data
        .map { preferences ->
            preferences[LANGUAGE_KEY] ?: "en"
        }

    suspend fun setDarkMode(isDarkMode: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[IS_DARK_MODE_KEY] = isDarkMode
        }
    }
    
    suspend fun setLanguage(language: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }
}
