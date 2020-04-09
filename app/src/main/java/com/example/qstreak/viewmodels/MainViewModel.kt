package com.example.qstreak.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.QstreakDatabase
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.models.Submission
import com.example.qstreak.models.User
import com.example.qstreak.network.Account
import com.example.qstreak.network.CreateUserRequest
import com.example.qstreak.network.QstreakApiService
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var submissions: LiveData<List<Submission>> = MutableLiveData()
    private val repository = SubmissionRepository(QstreakDatabase.getInstance(application).submissionDao())
    private val api: QstreakApiService by lazy {
        QstreakApiService.getQstreakApiService()
    }

    init {
        submissions = repository.submissions

        viewModelScope.launch {
            api.signup(CreateUserRequest(Account(40, 2, "12345")))
        }
    }

    fun createSubmission(submission: Submission) {
        viewModelScope.launch {
            repository.insert(submission)
        }
    }
}