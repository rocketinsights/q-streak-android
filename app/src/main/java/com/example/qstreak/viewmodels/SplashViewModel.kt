package com.example.qstreak.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qstreak.utils.UID

class SplashViewModel(sharedPrefs: SharedPreferences) : ViewModel() {

    val isUserRegistered = MutableLiveData<Boolean>()

    init {
        isUserRegistered.value = sharedPrefs.getString(UID, null) != null
    }
}
