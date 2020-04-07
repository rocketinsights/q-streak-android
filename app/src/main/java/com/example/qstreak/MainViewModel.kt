package com.example.qstreak

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.qstreak.models.Submission

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val submissions = MutableLiveData<List<Submission>>()

    init {
        submissions.value = listOf<Submission>(
            Submission("2020-04-7", 10)
        )
    }
}