package com.example.qstreak.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qstreak.utils.UID
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(sharedPrefs: SharedPreferences) : ViewModel() {

    val isUserRegistered = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            delay(2000)

            isUserRegistered.value = sharedPrefs.getString(UID, null) != null
        }
    }
}
