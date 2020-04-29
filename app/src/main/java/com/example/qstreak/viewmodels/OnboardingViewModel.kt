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

    val name = MutableLiveData<String>()
    val zipCode = MutableLiveData<String>()
    val zipCodeError = MutableLiveData<Boolean>(false)

    fun createUser() {
        val name = name.value
        val zipCode = zipCode.value

        if (!validateInputs(name, zipCode)) {
            return
        }

        viewModelScope.launch {
            try {
                val createUserResponse =
                    userRepository.createUser(name, zipCode!!)
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

    private fun validateInputs(name: String?, zipCode: String?): Boolean {
        clearErrors()

        val zipValid = isZipValid(zipCode)
        val nameValid = isNameValid(name)
        // Ideally we'll set errors on each of the invalid fields, so we don't want to return until
        // they have all been evaluated.
        if (!zipValid) {
            // Could also set a custom error string based on exactly how it is invalid,
            // but for now just a single "invalid" message.
            zipCodeError.value = true
        }
        return zipValid && nameValid
    }

    // TODO These methods could be extracted to a utility class.

    private fun isZipValid(zip: String?): Boolean {
        return !zip.isNullOrBlank() && zip.length == 5 && zip.toInt() > 0
    }

    private fun isNameValid(name: String?): Boolean {
        // TODO validation rules - character validation?
        return true
    }

    private fun clearErrors() {
        zipCodeError.value = false
    }
}
