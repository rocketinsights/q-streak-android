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
import com.example.qstreak.utils.DateUtils
import com.example.qstreak.viewmodels.SubmissionsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SubmissionDetailFragment : Fragment() {
    private val submissionsViewModel: SubmissionsViewModel by lazy {
        (requireActivity() as MainActivity).submissionsViewModel
    }
    private lateinit var binding: FragmentSubmissionDetailBinding

    override fun onResume() {
        super.onResume()
        submissionsViewModel.refreshDate()
    }

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

        setupEditClickListener()
        observeDate()
        observeDeletion()
        setupActivitiesList()
        setupBackButtonClickListener()
        setupDeleteButtonClickListener()

        return binding.root
    }

    private fun setupEditClickListener() {
        binding.editButton.setOnClickListener {
            submissionsViewModel.selectedDateString.value?.let {
                (requireActivity() as MainActivity).navigateToAddOrEditRecord(it)
            } ?: run {
                (requireActivity() as MainActivity).navigateToAddOrEditRecord()
            }

        }
    }

    private fun setupBackButtonClickListener() {
        binding.submissionDetailBackButton.setOnClickListener {
            (requireActivity() as MainActivity).onBackPressed()
        }
    }

    private fun setupDeleteButtonClickListener() {
        binding.deleteButton.setOnClickListener {
            context?.let {
                MaterialAlertDialogBuilder(it, R.style.ConfirmationDialogTheme)
                    .setTitle(getString(R.string.delete_record_confirmation_title))
                    .setMessage(getString(R.string.delete_record_confirmation_message))
                    .setNeutralButton(getString(R.string.delete_record_confirmation_cancel)) { _, _ ->
                        // do nothing
                    }
                    .setPositiveButton(getString(R.string.delete_record_confirmation_confirm)) { _, _ ->
                        submissionsViewModel.deleteSubmission()
                    }
                    .show()
            }
        }
    }

    private fun observeDate() {
        submissionsViewModel.selectedDateString.observe(viewLifecycleOwner, Observer {
            binding.submissionDetailDate.text = DateUtils.getDateDisplayStringFromDbRecord(it)
        })
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

        fun newInstance(): SubmissionDetailFragment {
            return SubmissionDetailFragment()
        }
    }
}
