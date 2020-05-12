package com.example.qstreak.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qstreak.R
import com.example.qstreak.databinding.FragmentDashboardBinding
import com.example.qstreak.models.DailyLogItemInfo
import com.example.qstreak.utils.DateUtils
import com.example.qstreak.utils.RecyclerViewUtils
import com.example.qstreak.viewmodels.DashboardViewModel
import com.example.qstreak.viewmodels.SubmissionsViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.help_card.view.*
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
        dashboardViewModel.refreshToday()
        dashboardViewModel.refreshDashboardMessages()
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

        binding.scoreMeter.setOnClickListener {
            getTodaysDailyLogInfo()?.let { it ->
                onDailyLogItemSelected(it)
            }
        }

        setupDailyLog()
        setupDashboardMessages()
        helpButtonClickListener()
        profileButtonClickListener()

        return binding.root
    }

    private fun getTodaysDailyLogInfo(): DailyLogItemInfo? {
        return submissionsViewModel.dailyLogInfos.value?.find {
            it.isToday
        }
    }

    private fun setupDailyLog() {
        val recyclerView = binding.dailyLogWeekView
        val adapter = DailyLogAdapter(this::onDailyLogItemSelected)
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

    private fun onDailyLogItemSelected(item: DailyLogItemInfo) {
        if (item.isComplete) {
            submissionsViewModel.selectDate(DateUtils.dateStringFormat.format(item.date))
            (requireActivity() as MainActivity).navigateToShowRecord()
        } else {
            (requireActivity() as MainActivity).navigateToAddOrEditRecord(
                DateUtils.dateStringFormat.format(item.date)
            )
        }
    }

    private fun onScrollFirstItemVisible(firstItemPosition: Int) {
        submissionsViewModel.setCurrentWeekBasedOnScrollPosition(firstItemPosition)
    }

    private fun setupDashboardMessages() {
        val recyclerView = binding.dashboardMessages
        val adapter = DashboardMessagesAdapter(this::openUrl)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        dashboardViewModel.dashboardMessages.observe(viewLifecycleOwner, Observer {
            adapter.setMessages(it)
        })
    }

    private fun helpButtonClickListener() {
        binding.helpButton.setOnClickListener {
            val window = PopupWindow(requireContext())
            val view = layoutInflater.inflate(R.layout.help_card, null)
            window.contentView = view
            window.width = LinearLayout.LayoutParams.MATCH_PARENT
            window.height = LinearLayout.LayoutParams.MATCH_PARENT
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.showAtLocation(view, Gravity.BOTTOM, 0, 0)

            window.contentView.help_header.text = getString(R.string.todays_score_header)
            window.contentView.help_text.text = getString(R.string.todays_score_text)

            val closeWindow = view.findViewById<ExtendedFloatingActionButton>(R.id.close_window)
            closeWindow.setOnClickListener {
                window.dismiss()
            }
        }
    }

    private fun profileButtonClickListener() {
        binding.profileButton.setOnClickListener {
            (requireActivity() as MainActivity).navigateToShowProfile()
        }
    }

    private fun openUrl(url: String?) {
        url?.let {
            val openUrl = Intent(Intent.ACTION_VIEW)
            try {
                openUrl.data = Uri.parse("https://$url")
                startActivity(openUrl)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}
