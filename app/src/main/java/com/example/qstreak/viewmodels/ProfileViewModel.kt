package com.example.qstreak.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.UserRepository
import com.example.qstreak.network.ApiResult
import com.example.qstreak.utils.UID
import com.example.qstreak.utils.USER_NAME
import com.example.qstreak.utils.USER_ZIP
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val sharedPrefs: SharedPreferences,
    private val userRepository: UserRepository,
    private val sharedPrefsEditor: SharedPreferences.Editor
) : ViewModel() {
    val userName = MutableLiveData<String>(sharedPrefs.getString(USER_NAME, null))
    val userZipCode = MutableLiveData<String>(sharedPrefs.getString(USER_ZIP, null))
    val nameError = MutableLiveData<Boolean>(false)
    val zipCodeError = MutableLiveData<Boolean>(false)
    val profileUpdated = MutableLiveData<Boolean>(false)
    var userNameDisplay = sharedPrefs.getString(USER_NAME, null)
    var userZipCodeDisplay = sharedPrefs.getString(USER_ZIP, null)

    private val uid: String? by lazy {
        sharedPrefs.getString(UID, null)
    }

    // Used in display of Edit Profile Name/Zip Code screens
    val errorToDisplay = MutableLiveData<String>()

    fun saveProfile() {
        val name = userName.value?.let { it } ?: ""
        val zipCode = userZipCode.value

        if (!validateInputs(name, zipCode)) {
            return
        }

        try {
            viewModelScope.launch {
                val response =
                    userRepository.updateUser(name, zipCode!!, uid.toString())

                if (response is ApiResult.Success) {
                    sharedPrefsEditor.putString(USER_NAME, name).commit()
                    sharedPrefsEditor.putString(USER_ZIP, zipCode).commit()
                    refreshNameAndZipCode()
                    profileUpdated.value = true
                } else {
                    errorToDisplay.value = (response as ApiResult.Error).apiErrors
                }
            }
        } catch (e: Exception) {
            errorToDisplay.value = e.message
        }
    }

    private fun refreshNameAndZipCode() {
        userNameDisplay = sharedPrefs.getString(USER_NAME, null)
        userZipCodeDisplay = sharedPrefs.getString(USER_ZIP, null)
    }

    private fun validateInputs(name: String?, zipCode: String?): Boolean {
        val zipValid = isZipValid(zipCode)
        val nameValid = isNameValid(name)
        zipCodeError.value = !zipValid
        nameError.value = !nameValid
        return zipValid && nameValid
    }

    private fun isZipValid(zip: String?): Boolean {
        return !zip.isNullOrBlank() && zip.length == 5 && zip.toInt() > 0
    }

    private fun isNameValid(name: String?): Boolean {
        // As of now name is optional.
        return true
    }

}
