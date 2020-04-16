package com.example.qstreak.ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.qstreak.R

class SubmissionsActivity : FragmentActivity(R.layout.activity_submissions) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragment = SubmissionsListFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_view, fragment)
            .commit()
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
