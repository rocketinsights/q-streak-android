package com.example.qstreak.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qstreak.R
import com.example.qstreak.databinding.FragmentSubmissionsListBinding
import com.example.qstreak.models.SubmissionActivitiesPair
import com.example.qstreak.viewmodels.SubmissionsViewModel

class SubmissionsListFragment : Fragment() {

    private val submissionsViewModel: SubmissionsViewModel by activityViewModels()

    private lateinit var binding: FragmentSubmissionsListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_submissions_list,
            null,
            false
        )

        setupSubmissionsList()
        setAddSubmissionClickListener()

        return binding.root
    }

    private fun setupSubmissionsList() {
        val recyclerView = binding.submissionsRecyclerView
        val adapter = SubmissionsAdapter(this::onSubmissionSelected)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        submissionsViewModel.submissions.observe(viewLifecycleOwner, Observer { submissions ->
            submissions?.let { adapter.setSubmissions(it) }
        })
    }

    private fun setAddSubmissionClickListener() {
        binding.addSubmissionButton.setOnClickListener {
            navigateToAddSubmissionFragment()
        }
    }

    private fun onSubmissionSelected(submissionActivitiesPair: SubmissionActivitiesPair) {
        submissionsViewModel.select(submissionActivitiesPair)
        navigateToDetailFragment()
    }

    private fun navigateToAddSubmissionFragment() {
        (requireActivity() as SubmissionsActivity).navigateToAddSubmission()
    }

    private fun navigateToDetailFragment() {
        (requireActivity() as SubmissionsActivity).navigateToSubmissionDetail()
    }

    companion object {
        const val TAG = "SubmissionsListFragment"
    }
}