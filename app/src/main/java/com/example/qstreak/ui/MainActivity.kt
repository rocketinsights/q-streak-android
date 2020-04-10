package com.example.qstreak.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qstreak.R
import com.example.qstreak.databinding.ActivityMainBinding
import com.example.qstreak.models.Submission
import com.example.qstreak.viewmodels.MainViewModel

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
            showNewSubmissionDialog()
        }
    }

    private fun showNewSubmissionDialog() {
        NewSubmissionFragment(this::onNewSubmissionSubmitted).show(
            supportFragmentManager,
            "fragment_new_submission"
        )
    }

    private fun onNewSubmissionSubmitted(submission: Submission) {
        mainViewModel.createSubmission(submission, applicationContext)
    }
}
