package com.example.qstreak.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.UserRepository
import com.example.qstreak.utils.EncryptedSharedPreferencesUtil
import kotlinx.coroutines.launch
import timber.log.Timber

class OnboardingViewModel(private val userRepository: UserRepository) : ViewModel() {
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
