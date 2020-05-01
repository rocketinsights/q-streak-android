package com.example.qstreak.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qstreak.R
import com.example.qstreak.databinding.FragmentTempDailyLogBinding
import com.example.qstreak.models.DailyLogItemInfo
import com.example.qstreak.viewmodels.SubmissionsViewModel

class TempDailyLogFragment : Fragment() {

    private val submissionsViewModel: SubmissionsViewModel by lazy {
        (requireActivity() as MainActivity).submissionsViewModel
    }

    private lateinit var binding: FragmentTempDailyLogBinding

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
            R.layout.fragment_temp_daily_log,
            null,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner

        setupDailyLog()

        return binding.root
    }

    private fun setupDailyLog() {
        val recyclerView = binding.dailyLogWeekView
        val adapter = DailyLogAdapter(this::onItemSelected)
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        submissionsViewModel.dailyLogInfos.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { infos ->
                adapter.setItemInfos(infos)
                recyclerView.layoutManager?.scrollToPosition(adapter.itemCount - 1)
            })
    }

    private fun onItemSelected(item: DailyLogItemInfo) {
        (requireActivity() as MainActivity).navigateToAddOrEditRecord(item.submission?.submission?.date)
    }

    companion object {
        const val TAG = "TempDailyLogFragment"
    }
}
