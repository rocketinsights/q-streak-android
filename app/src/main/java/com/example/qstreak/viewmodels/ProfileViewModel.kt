package com.example.qstreak.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qstreak.utils.USER_NAME
import com.example.qstreak.utils.USER_ZIP

class ProfileViewModel(
    sharedPrefs: SharedPreferences
) : ViewModel() {

    val userName = MutableLiveData<String>(sharedPrefs.getString(USER_NAME, null))
    val userZipCode = MutableLiveData<String>(sharedPrefs.getString(USER_ZIP, null))

}
