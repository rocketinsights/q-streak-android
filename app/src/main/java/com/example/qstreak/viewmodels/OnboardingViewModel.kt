package com.example.qstreak.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.QstreakDatabase
import com.example.qstreak.db.UserRepository
import com.example.qstreak.utils.EncryptedSharedPreferencesUtil
import kotlinx.coroutines.launch

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(QstreakDatabase.getInstance(application).userDao())
    val signupSuccessful = MutableLiveData<Boolean>(false)

    fun createUser(context: Context, age: Int, householdSize: Int, zip: String) {
        val sharedPreferences =
            EncryptedSharedPreferencesUtil.getEncryptedSharedPreferences(context)

        viewModelScope.launch {
            try {
                val newUser = userRepository.createUser(age, householdSize, zip)
                sharedPreferences.edit().putString(UID_KEY, newUser.device_uid).apply()
                signupSuccessful.postValue(true)
            } catch (e: Exception) {
                Log.e("Create User Error", "Error message: " + e.message)
            }
        }
    }

    companion object {
        private const val UID_KEY = "uid"
    }
}