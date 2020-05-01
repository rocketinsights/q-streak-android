package com.example.qstreak.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qstreak.R
import com.example.qstreak.databinding.FragmentDashboardBinding
import com.example.qstreak.models.DailyLogItemInfo
import com.example.qstreak.models.SubmissionWithActivities
import com.example.qstreak.utils.RecyclerViewUtils
import com.example.qstreak.viewmodels.DashboardViewModel
import com.example.qstreak.viewmodels.SubmissionsViewModel
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private val dashboardViewModel: DashboardViewModel by currentScope.viewModel(this)
    private val submissionsViewModel: SubmissionsViewModel by lazy {
        (requireActivity() as MainActivity).submissionsViewModel
    }

    override fun onResume() {
        super.onResume()
        submissionsViewModel.generateDailyLogInfos()
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
        binding.dashboardViewModel = dashboardViewModel
        binding.submissionsViewModel = submissionsViewModel

        binding.buttonRecordActivity.setOnClickListener {
            (requireActivity() as MainActivity).navigateToAddOrEditRecord()
        }

        setupDailyLog()

        return binding.root
    }

    private fun setupDailyLog() {
        val recyclerView = binding.dailyLogWeekView
        val adapter = DailyLogAdapter(this::onDateSelected)
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.addOnScrollListener(RecyclerViewUtils.getListenerForFirstVisibleItemPosition {
            onScrollFirstItemVisible(it)
        })

        submissionsViewModel.dailyLogInfos.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { infos ->
                adapter.setItemInfos(infos)
                recyclerView.layoutManager?.scrollToPosition(adapter.itemCount - 1)
            })
    }

    private fun onDateSelected(item: DailyLogItemInfo) {
        (requireActivity() as MainActivity).navigateToAddOrEditRecord(item.submission?.submission?.date)
    }

    private fun onSubmissionSelected(submissionWithActivities: SubmissionWithActivities) {
        submissionsViewModel.selectSubmission(submissionWithActivities)
        navigateToDetailFragment()
    }

    private fun navigateToDetailFragment() {
        (requireActivity() as MainActivity).navigateToShowRecord()
    }

    private fun onScrollFirstItemVisible(firstItemPosition: Int) {
        Timber.d("First item: %d", firstItemPosition)
        submissionsViewModel.setCurrentWeekBasedOnScrollPosition(firstItemPosition)
    }
}
