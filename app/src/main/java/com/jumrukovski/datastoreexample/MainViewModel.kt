package com.jumrukovski.datastoreexample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val preferencesManager: PreferencesManager) : ViewModel() {

    private val _appPreferences = MutableLiveData<PreferencesManager.AppPreferences>()
    val appPreferences:LiveData<PreferencesManager.AppPreferences> = _appPreferences

    fun updateStringValue(value:String){
        viewModelScope.launch(Dispatchers.IO) {
            preferencesManager.updateStringValue(value.trim())
        }
    }

    fun updateBooleanValue(value:Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            preferencesManager.updateBooleanValue(value)
        }
    }

    fun getAppPreferences(){
        viewModelScope.launch(Dispatchers.IO) {
            preferencesManager.preferencesFlow.collect{
                _appPreferences.postValue(it)
            }
        }
    }
}