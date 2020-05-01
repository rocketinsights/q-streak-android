package com.example.qstreak.ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.qstreak.R
import com.example.qstreak.viewmodels.SubmissionsViewModel
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : FragmentActivity(R.layout.activity_main) {
    val submissionsViewModel: SubmissionsViewModel by currentScope.viewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragment = DashboardFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_view, fragment)
            .commit()
    }

    fun navigateToShowRecord() {
        val fragment = SubmissionDetailFragment()
        supportFragmentManager.beginTransaction()
            .addToBackStack(SubmissionDetailFragment.TAG)
            .replace(R.id.fragment_container_view, fragment)
            .commit()
    }
    
    fun navigateToAddOrEditRecord(existingSubmissionDate: String? = null) {
        val fragment = AddEditSubmissionFragment.newInstance(existingSubmissionDate)
        supportFragmentManager.beginTransaction()
            .addToBackStack(AddEditSubmissionFragment.TAG)
            .replace(R.id.fragment_container_view, fragment)
            .commit()
    }
}
