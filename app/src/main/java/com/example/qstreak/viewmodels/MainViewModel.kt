package com.example.qstreak.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.QstreakDatabase
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.models.Submission
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var submissions: LiveData<List<Submission>> = MutableLiveData()
    val repository = SubmissionRepository(QstreakDatabase.getInstance(application).submissionDao())

    init {
        submissions = repository.submissions
    }

    fun createSubmission(submission: Submission) {
        viewModelScope.launch {
            repository.insert(submission)
        }
    }
}