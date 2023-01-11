package com.jumrukovski.datastoreexample

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class PreferencesManager(private val dataStore: DataStore<Preferences>) {

    companion object {
        val KEY_STRING = stringPreferencesKey("keyString")
        val KEY_BOOLEAN = booleanPreferencesKey("keyBoolean")
    }

    val preferencesFlow: Flow<AppPreferences> = dataStore.data.catch {
        emit(emptyPreferences())
    }.map { appPreferences ->
        val valueString: String = appPreferences[KEY_STRING] ?: ""
        val valueBoolean: Boolean = appPreferences[KEY_BOOLEAN] ?: false

        AppPreferences(valueString, valueBoolean)
    }

    suspend fun updateStringValue(valueString: String) {
        dataStore.edit { appPreferences ->
            appPreferences[KEY_STRING] = valueString
        }
    }

    suspend fun updateBooleanValue(valueBoolean: Boolean) {
        dataStore.edit { appPreferences ->
            appPreferences[KEY_BOOLEAN] = valueBoolean
        }
    }

    data class AppPreferences(val keyString: String, val keyBoolean: Boolean)
}