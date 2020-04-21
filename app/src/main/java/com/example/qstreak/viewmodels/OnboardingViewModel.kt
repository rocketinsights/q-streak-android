package com.example.qstreak.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.UserRepository
import com.example.qstreak.utils.UID
import kotlinx.coroutines.launch
import timber.log.Timber

class OnboardingViewModel(
    private val userRepository: UserRepository,
    private val sharedPrefsEditor: SharedPreferences.Editor
) : ViewModel() {
    val signupSuccessful = MutableLiveData<Boolean>(false)

    val age = MutableLiveData<String>()
    val householdSize = MutableLiveData<String>()
    val zipCode = MutableLiveData<String>()

    fun createUser() {
        val age = age.value
        val householdSize = householdSize.value
        val zipCode = zipCode.value
        if (age.isNullOrBlank() || householdSize.isNullOrBlank() || zipCode.isNullOrBlank()) {
            // TODO data validation - Post a LiveData containing errors to be handled by the Fragment
            return
        }

        viewModelScope.launch {
            try {
                val newUser = userRepository.createUser(age.toInt(), householdSize.toInt(), zipCode)
                sharedPrefsEditor.putString(UID, newUser.device_uid).commit()
                signupSuccessful.postValue(true)
            } catch (e: Exception) {
                Timber.e("Error message: %s", e.message)
            }
        }
    }
}
