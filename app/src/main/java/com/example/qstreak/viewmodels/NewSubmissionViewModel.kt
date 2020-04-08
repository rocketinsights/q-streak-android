package com.example.qstreak.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewSubmissionViewModel : ViewModel() {
    // TODO what happens on submission if blank values

    val date: LiveData<String> = MutableLiveData()
    val contactCount: LiveData<String> = MutableLiveData()
}