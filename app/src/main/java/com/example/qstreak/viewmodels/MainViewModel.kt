package com.example.qstreak.viewmodels

import android.app.Application
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
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var submissions: LiveData<List<Submission>> = MutableLiveData()
    private val submissionRepository =
        SubmissionRepository(QstreakDatabase.getInstance(application).submissionDao())
    private val userRepository = UserRepository(QstreakDatabase.getInstance(application).userDao())
    private val signupApi: QstreakApiSignupService by lazy {
        QstreakApiSignupService.getQstreakApiSignupService()
    }

    private var currentUser: User? = null

    init {
        submissions = submissionRepository.submissions

        viewModelScope.launch {
           currentUser = userRepository.getUser()
            // if there is no user in the database
            if (currentUser == null) {
                // TODO best way to handle different types of responses
                val response = signupApi.signup(CreateUserRequest(Account(40, 2, "02906")))
                if (response.uid.isNotEmpty()) {
                    // TODO add type converter to Response object
                    val newUser =
                        User(response.zip, response.age, response.householdSize, response.uid)
                    userRepository.insert(newUser)
                    currentUser = newUser
                }
            }
        }
    }

    fun createSubmission(submission: Submission) {
        val api = QstreakApiService.getQstreakApiService(currentUser!!.device_uid)

        viewModelScope.launch {
            // TODO convert submission model
            val response = api.createSubmission(CreateSubmissionRequest(Submission(submission.contactCount, submission.date, emptyList())))
            // TODO handle HTTP response codes
            if (response.id >= 0) {
                submissionRepository.insert(submission)
            } else {
                Log.e("Submission Request", "Submission Request Failed")
            }
        }
    }
}