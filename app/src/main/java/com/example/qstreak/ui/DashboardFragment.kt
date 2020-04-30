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
import com.example.qstreak.databinding.FragmentDashboardBinding
import com.example.qstreak.models.SubmissionWithActivities
import com.example.qstreak.viewmodels.DashboardViewModel
import com.example.qstreak.viewmodels.SubmissionsViewModel
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private val dashboardViewModel: DashboardViewModel by currentScope.viewModel(this)
    private val submissionsViewModel: SubmissionsViewModel by lazy {
        (requireActivity() as MainActivity).submissionsViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_dashboard,
            null,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = dashboardViewModel

        binding.buttonRecordActivity.setOnClickListener {
            (requireActivity() as MainActivity).navigateToAddOrEditRecord()
        }

        binding.buttonDebugComponent.setOnClickListener {
            navigateToTempDailyLogFragment()
        }

        setupSubmissionsList()

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

    private fun onSubmissionSelected(submissionWithActivities: SubmissionWithActivities) {
        submissionsViewModel.selectSubmission(submissionWithActivities)
        navigateToDetailFragment()
    }

    private fun navigateToDetailFragment() {
        (requireActivity() as MainActivity).navigateToShowRecord()
    }

    private fun navigateToTempDailyLogFragment() {
        (requireActivity() as MainActivity).navigateToTempDailyLog()
    }
}
