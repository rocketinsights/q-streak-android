package com.example.qstreak.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {
    val testString = MutableLiveData("Data from dashboard viewmodel")
}
