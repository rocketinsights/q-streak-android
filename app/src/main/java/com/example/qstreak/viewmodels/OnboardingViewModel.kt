package com.example.qstreak.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.QstreakDatabase
import com.example.qstreak.db.UserRepository
import com.example.qstreak.utils.EncryptedSharedPreferencesUtil
import kotlinx.coroutines.launch
import timber.log.Timber

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(QstreakDatabase.getInstance(application).userDao())
    val signupSuccessful = MutableLiveData<Boolean>(false)

    fun createUser(context: Context, age: Int, householdSize: Int, zip: String) {
        viewModelScope.launch {
            try {
                val newUser = userRepository.createUser(age, householdSize, zip)
                EncryptedSharedPreferencesUtil.setUid(
                    context,
                    newUser.device_uid
                )
                signupSuccessful.postValue(true)
            } catch (e: Exception) {
                Timber.e("Error message: %s", e.message)
            }
        }
    }
}
