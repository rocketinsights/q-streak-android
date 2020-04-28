package com.example.qstreak.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.qstreak.R
import com.example.qstreak.databinding.ActivityDashboardBinding
import com.example.qstreak.viewmodels.DashboardViewModel
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDashboardBinding
    val viewModel:DashboardViewModel by currentScope.viewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        binding.buttonRecordActivity.setOnClickListener {
            navigateToAddSubmissionActivity()
        }
    }

    private fun navigateToAddSubmissionActivity() {
        val intent = Intent(this, SubmissionsActivity::class.java)
        startActivity(intent)
    }
}