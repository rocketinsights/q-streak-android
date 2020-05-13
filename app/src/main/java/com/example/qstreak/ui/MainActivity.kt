package com.example.qstreak.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.qstreak.R
import com.example.qstreak.utils.DateUtils
import com.example.qstreak.viewmodels.SubmissionsViewModel
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

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
        val fragment = SubmissionDetailFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.pull_out_from_bottom, 0, 0, 0)
            .addToBackStack(SubmissionDetailFragment.TAG)
            .replace(R.id.fragment_container_view, fragment)
            .commit()
    }

    fun navigateToAddOrEditRecord(
        existingSubmissionDate: String = DateUtils.dateStringFormat.format(
            Calendar.getInstance().time
        )
    ) {
        val fragment = AddEditSubmissionFragment.newInstance(existingSubmissionDate)
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.pull_out_from_bottom, 0, 0, 0)
            .addToBackStack(AddEditSubmissionFragment.TAG)
            .replace(R.id.fragment_container_view, fragment)
            .commit()
    }

    fun navigateToShowProfile() {
        val fragment = ProfileFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.pull_out_from_bottom, 0, 0, 0)
            .addToBackStack(ProfileFragment.TAG)
            .replace(R.id.fragment_container_view, fragment)
            .commit()
    }

    fun navigateToEditProfile() {
        val fragment = EditProfileFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.pull_out_from_bottom, 0, 0, 0)
            .addToBackStack(EditProfileFragment.TAG)
            .replace(R.id.fragment_container_view, fragment)
            .commit()
    }

    fun navigateToEditProfileName() {
        val fragment = EditProfileNameFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.pull_out_from_bottom, 0, 0, 0)
            .addToBackStack(EditProfileNameFragment.TAG)
            .replace(R.id.fragment_container_view, fragment)
            .commit()
    }

    fun navigateToEditProfileZipCode() {
        val fragment = EditProfileZipCodeFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.pull_out_from_bottom, 0, 0, 0)
            .addToBackStack(EditProfileZipCodeFragment.TAG)
            .replace(R.id.fragment_container_view, fragment)
            .commit()
    }

    companion object {
        fun newInstance(context: Context, flags: Int): Intent {
            return Intent(context, MainActivity::class.java).apply {
                this.flags = flags
            }
        }
    }
}
