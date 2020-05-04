package com.example.qstreak.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qstreak.R
import com.example.qstreak.databinding.FragmentSubmissionDetailBinding
import com.example.qstreak.viewmodels.SubmissionsViewModel

class SubmissionDetailFragment : Fragment() {
    private val submissionsViewModel: SubmissionsViewModel by lazy {
        (requireActivity() as MainActivity).submissionsViewModel
    }
    private lateinit var binding: FragmentSubmissionDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_submission_detail,
            null,
            false
        )
        binding.lifecycleOwner = activity
        binding.viewModel = submissionsViewModel

        observeDeletion()
        setupActivitiesList()

        return binding.root
    }

    private fun observeDeletion() {
        submissionsViewModel.submissionDeleted.observe(viewLifecycleOwner, Observer {
            if (it) {
                requireActivity().supportFragmentManager.popBackStack()
                // TODO replace with SingleLiveEvent so there's no action required here
                submissionsViewModel.submissionDeleted.value = false
            }
        })
    }

    private fun setupActivitiesList() {
        val adapter = ActivitiesListAdapter(
            submissionsViewModel.selectedSubmission.value?.activities.orEmpty()
        )

        submissionsViewModel.selectedSubmission.observe(viewLifecycleOwner, Observer {
            adapter.setActivities(it.activities)
        })

        binding.activitiesList.adapter = adapter
        binding.activitiesList.layoutManager =
            LinearLayoutManager(activity)
    }

    companion object {
        const val TAG = "SubmissionDetailFragment"
    }
}
