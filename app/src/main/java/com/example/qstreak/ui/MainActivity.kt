package com.example.qstreak.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qstreak.viewmodels.MainViewModel
import com.example.qstreak.R
import com.example.qstreak.databinding.ActivityMainBinding
import com.example.qstreak.models.Submission
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val recyclerView = binding.submissionsRecyclerView
        val adapter = SubmissionsAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        mainViewModel.submissions.observe(this, Observer { submissions ->
            submissions?.let { adapter.setSubmissions(it) }
        })

        binding.addSubmissionButton.setOnClickListener {
            mainViewModel.createSubmission(Submission("12398120398", 123))
        }
    }
}
