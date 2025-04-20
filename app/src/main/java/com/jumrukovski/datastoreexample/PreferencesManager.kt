package com.jumrukovski.datastoreexample

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class PreferencesManager(private val dataStore: DataStore<Preferences>) {

    companion object {
        val KEY_NAME = stringPreferencesKey("keyString")
        val KEY_IS_STUDENT = booleanPreferencesKey("keyBoolean")
    }

    val preferencesFlow: Flow<AppPreferences> = dataStore.data.catch {
        emit(emptyPreferences())
    }.map { appPreferences ->
        val name: String = appPreferences[KEY_NAME] ?: ""
        val isStudent: Boolean = appPreferences[KEY_IS_STUDENT] ?: false

        AppPreferences(name, isStudent)
    }

    suspend fun updateName(name: String) {
        dataStore.edit { appPreferences ->
            appPreferences[KEY_NAME] = name
        }
    }

    suspend fun updateIsStudent(isStudent: Boolean) {
        dataStore.edit { appPreferences ->
            appPreferences[KEY_IS_STUDENT] = isStudent
        }
    }

    data class AppPreferences(val name: String = "", val isStudent: Boolean = false)
}