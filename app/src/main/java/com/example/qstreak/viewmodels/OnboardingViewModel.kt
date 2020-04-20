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

    val age = MutableLiveData<String>()
    val householdSize = MutableLiveData<String>()
    val zipCode = MutableLiveData<String>()

    fun createUser(context: Context) {
        val age = age.value
        val householdSize = householdSize.value
        val zipCode = zipCode.value
        if (age.isNullOrBlank() || householdSize.isNullOrBlank() || zipCode.isNullOrBlank()) {
            // Post a LiveData containing errors to be handled by the Fragment
            return
        }

        viewModelScope.launch {
            try {
                val newUser = userRepository.createUser(age.toInt(), householdSize.toInt(), zipCode)
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
