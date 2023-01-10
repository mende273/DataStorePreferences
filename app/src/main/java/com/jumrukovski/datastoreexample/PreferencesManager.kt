package com.jumrukovski.datastoreexample

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "preferences")

class PreferencesManager(private val context: Context) {

    companion object {
        val KEY_STRING = stringPreferencesKey("keyString")
        val KEY_BOOLEAN = booleanPreferencesKey("keyBoolean")
    }

    val preferencesFlow: Flow<Preferences> = context.dataStore.data.catch {
        emit(emptyPreferences())
    }.map { preferences ->
        val valueString: String = preferences[KEY_STRING] ?: ""
        val valueBoolean: Boolean = preferences[KEY_BOOLEAN] ?: false

        Preferences(valueString,valueBoolean)
    }

    suspend fun updateStringValue(valueString: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_STRING] = valueString
        }
    }

    suspend fun updateBooleanValue(valueBoolean: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_BOOLEAN] = valueBoolean
        }
    }

    data class Preferences(val keyString: String, val keyBoolean: Boolean)
}