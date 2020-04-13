package com.example.qstreak.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.QstreakDatabase
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.models.Submission
import kotlinx.coroutines.launch

class SubmissionDetailViewModel(application: Application, id: Int) : AndroidViewModel(application) {
    private val submissionRepository =
        SubmissionRepository(QstreakDatabase.getInstance(application).submissionDao())

    var submission: Submission? = null

    init {
        viewModelScope.launch {
            submission = submissionRepository.getSubmissionById(id)
        }
    }
}