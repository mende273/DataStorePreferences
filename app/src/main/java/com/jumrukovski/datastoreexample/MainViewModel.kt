package com.jumrukovski.datastoreexample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val preferencesManager: PreferencesManager) :
    ViewModel() {

    val appPreferences: StateFlow<PreferencesManager.AppPreferences> = preferencesManager
        .preferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PreferencesManager.AppPreferences()
        )

    fun updateStringValue(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesManager.updateName(value.trim())
        }
    }

    fun updateBooleanValue(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesManager.updateIsStudent(value)
        }
    }
}