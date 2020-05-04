package com.example.qstreak.viewmodels

import android.text.format.DateUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qstreak.db.SubmissionRepository

class DashboardViewModel(private val submissionRepository: SubmissionRepository) : ViewModel() {
    val testString = MutableLiveData("Data from dashboard viewmodel")
}
