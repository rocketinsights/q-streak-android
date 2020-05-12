package com.example.qstreak.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qstreak.db.UserRepository
import com.example.qstreak.utils.USER_NAME

class UserViewModel(
    private val userRepository: UserRepository,
    sharedPrefs: SharedPreferences
) : ViewModel() {

    val userName = MutableLiveData<String>(sharedPrefs.getString(USER_NAME, null))

}
