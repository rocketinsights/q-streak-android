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
    val zipCodeError = MutableLiveData<Boolean>(false)

    fun createUser() {
        val age = age.value
        val householdSize = householdSize.value
        val zipCode = zipCode.value

        if (!validateInputs(age, householdSize, zipCode)) {
            return
        }

        viewModelScope.launch {
            try {
                val createUserResponse =
                    userRepository.createUser(age!!.toInt(), householdSize!!.toInt(), zipCode!!)
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

    private fun validateInputs(age: String?, householdSize: String?, zipCode: String?): Boolean {
        clearErrors()

        val zipValid = isZipValid(zipCode)
        val ageValid = isAgeValid(age)
        val householdSizeValid = isHouseholdSizeValid(householdSize)
        // Ideally we'll set errors on each of the invalid fields, so we don't want to return until
        // they have all been evaluated.
        if (!zipValid) {
            // Could also set a custom error string based on exactly how it is invalid,
            // but for now just a single "invalid" message.
            zipCodeError.value = true
        }
        return zipValid && ageValid && householdSizeValid
    }

    // TODO These methods could be extracted to a utility class.

    private fun isZipValid(zip: String?): Boolean {
        return !zip.isNullOrBlank() && zip.length == 5 && zip.toInt() > 0
    }

    private fun isAgeValid(age: String?): Boolean {
        // TODO validation rules
        return !age.isNullOrBlank()
    }

    private fun isHouseholdSizeValid(householdSize: String?): Boolean {
        // TODO validation rules
        return !householdSize.isNullOrBlank()
    }

    private fun clearErrors() {
        zipCodeError.value = false
    }
}
