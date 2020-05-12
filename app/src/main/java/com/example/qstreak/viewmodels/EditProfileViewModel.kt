package com.example.qstreak.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.UserRepository
import com.example.qstreak.models.Activity
import com.example.qstreak.network.ApiResult
import com.example.qstreak.network.SubmissionResponse
import com.example.qstreak.utils.UID
import com.example.qstreak.utils.USER_NAME
import com.example.qstreak.utils.USER_ZIP
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val userRepository: UserRepository,
    sharedPrefs: SharedPreferences
) : ViewModel() {

    val userName = MutableLiveData<String>(sharedPrefs.getString(USER_NAME, null))
    val userZipCode = MutableLiveData<String>(sharedPrefs.getString(USER_ZIP, null))
    private val uid: String? by lazy {
        sharedPrefs.getString(UID, null)
    }


    // Used in display of Edit Profile screen
    val errorToDisplay = MutableLiveData<String>()

    // Observe for when to close edit screen
    val profileComplete = MutableLiveData<Boolean>(false)


//    fun saveProfile() {
//        // TODO handle null uid
//        if (uid != null && isUserInputValid()) {
//            try {
//                viewModelScope.launch {
//                    val response = updateUser()
//
//                    if (response is ApiResult.Success) {
//                        submissionComplete.value = true
//                    } else {
//                        errorToDisplay.value = (response as ApiResult.Error).apiErrors
//                    }
//                }
//            } catch (e: Exception) {
//                errorToDisplay.value = e.message
//            }
//        }
//    }
//
//
//    private suspend fun updateUser(
//        date: String,
//        contactCount: Int,
//        activities: List<Activity>,
//        uid: String
//    ): ApiResult<SubmissionResponse> {
//        return userRepository.updateUser(name, zipCode, uid)
//    }

    private fun isUserInputValid(): Boolean {
        // TODO
        return true
    }

}
