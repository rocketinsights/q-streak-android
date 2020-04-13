package com.example.qstreak.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qstreak.R
import com.example.qstreak.databinding.ActivitySubmissionsBinding
import com.example.qstreak.models.Submission
import com.example.qstreak.viewmodels.SubmissionsViewModel

class SubmissionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubmissionsBinding
    private val submissionsViewModel: SubmissionsViewModel by lazy {
        ViewModelProvider(this).get(SubmissionsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_submissions)

        val recyclerView = binding.submissionsRecyclerView
        val adapter = SubmissionsAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        submissionsViewModel.submissions.observe(this, Observer { submissions ->
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
        submissionsViewModel.createSubmission(submission, applicationContext)
    }
}
