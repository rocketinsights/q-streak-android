package com.example.qstreak.ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.qstreak.R
import com.example.qstreak.viewmodels.SubmissionsViewModel
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class SubmissionsActivity : FragmentActivity(R.layout.activity_submissions) {
    val sharedViewModel: SubmissionsViewModel by currentScope.viewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragment = SubmissionsListFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_view, fragment)
            .commit()
        navigateToAddSubmission()
    }

    fun navigateToSubmissionDetail() {
        val fragment = SubmissionDetailFragment()
        supportFragmentManager.beginTransaction()
            .addToBackStack(SubmissionDetailFragment.TAG)
            .replace(R.id.fragment_container_view, fragment)
            .commit()
    }

    fun navigateToAddSubmission() {
        val fragment = AddSubmissionFragment()
        supportFragmentManager.beginTransaction()
            .addToBackStack(AddSubmissionFragment.TAG)
            .replace(R.id.fragment_container_view, fragment)
            .commit()
    }
}
