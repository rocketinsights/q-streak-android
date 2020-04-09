package com.example.qstreak.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.QstreakDatabase
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.db.UserRepository
import com.example.qstreak.models.Submission
import com.example.qstreak.models.User
import com.example.qstreak.network.*
import com.example.qstreak.utils.EncryptedSharedPreferencesUtil
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var submissions: LiveData<List<Submission>> = MutableLiveData()
    private val submissionRepository =
        SubmissionRepository(QstreakDatabase.getInstance(application).submissionDao())
    private val userRepository = UserRepository(QstreakDatabase.getInstance(application).userDao())
    private val signupApi: QstreakApiSignupService by lazy {
        QstreakApiSignupService.getQstreakApiSignupService()
    }

    init {
        submissions = submissionRepository.submissions

        // TODO move logic to onboarding
        createUserIfNoneExists(application)
    }

    fun createSubmission(submission: Submission, context: Context) {
        // get uid to pass as bearer token
        val uid = EncryptedSharedPreferencesUtil.getEncryptedSharedPreferences(context).getString(
            UID_KEY, null
        )

        // TODO handle case when trying to create a submission without uid
        val api = QstreakApiService.getQstreakApiService(uid!!)

        viewModelScope.launch {
            // TODO convert submission model
            val response = api.createSubmission(
                CreateSubmissionRequest(
                    Submission(
                        submission.contactCount,
                        submission.date,
                        emptyList()
                    )
                )
            )
            // TODO handle HTTP response codes
            if (response.id >= 0) {
                submissionRepository.insert(submission.copy(remoteId = response.id))
            } else {
                Log.e("Submission Request", "Submission Request Failed")
            }
        }
    }

    private fun createUserIfNoneExists(context: Context) {

        val sharedPreferences =
            EncryptedSharedPreferencesUtil.getEncryptedSharedPreferences(context)

        viewModelScope.launch {
            // if there is no UID stored
            if (sharedPreferences.getString(UID_KEY, null) == null) {
                // TODO best way to handle HTTP response codes
                val response = signupApi.signup(CreateUserRequest(Account(40, 2, "02906")))
                if (response.uid.isNotEmpty()) {
                    // TODO add type converter to Response object
                    val newUser =
                        User(response.zip, response.age, response.householdSize, response.uid)
                    userRepository.insert(newUser)
                    sharedPreferences.edit().putString(UID_KEY, newUser.device_uid).apply()
                }
            }
        }
    }

    companion object {
        private const val UID_KEY = "uid"
    }
}