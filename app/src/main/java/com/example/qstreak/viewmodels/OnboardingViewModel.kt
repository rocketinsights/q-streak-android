package com.example.qstreak.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.UserRepository
import com.example.qstreak.network.ApiResult
import com.example.qstreak.utils.UID
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val userRepository: UserRepository,
    private val sharedPrefsEditor: SharedPreferences.Editor
) : ViewModel() {
    val signupSuccessful = MutableLiveData<Boolean>(false)
    val errorToDisplay = MutableLiveData<String>()

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
                val createUserResponse =
                    userRepository.createUser(age.toInt(), householdSize.toInt(), zipCode)
                if (createUserResponse is ApiResult.Success) {
                    sharedPrefsEditor.putString(UID, createUserResponse.data.uid).commit()
                    signupSuccessful.postValue(true)
                } else {
                    // We received an API error response when attempting to create a user.
                    errorToDisplay.value = (createUserResponse as ApiResult.Error).apiErrors
                }
            } catch (e: Exception) {
                // Something else went wrong when attempting to create a user.
                errorToDisplay.value = e.message
            }
        }
    }
}
