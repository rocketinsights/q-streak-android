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
import com.example.qstreak.databinding.FragmentAddSubmissionBinding
import com.example.qstreak.models.Activity
import com.example.qstreak.viewmodels.AddSubmissionViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class AddEditSubmissionFragment : Fragment() {

    private val viewModel: AddSubmissionViewModel by currentScope.viewModel(this)
    private lateinit var binding: FragmentAddSubmissionBinding

    override fun onResume() {
        super.onResume()
        viewModel.refreshActivities()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_add_submission,
            null,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        setupActivitiesList()
        observeCompletion()
        setDateClickListener()

        return binding.root
    }

    private fun onSubmissionCompleted() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun setupActivitiesList() {
        val adapter = ActivitiesChecklistAdapter(
            this::onActivityToggled,
            viewModel.activities.value.orEmpty()
        )

        viewModel.activities.observe(viewLifecycleOwner, Observer {
            adapter.setActivities(it)
        })

        binding.activitiesChecklist.adapter = adapter
        binding.activitiesChecklist.layoutManager =
            LinearLayoutManager(activity)
    }

    private fun observeCompletion() {
        viewModel.submissionComplete.observe(viewLifecycleOwner, Observer {
            if (it) {
                onSubmissionCompleted()
            }
        })
    }

    private fun setDateClickListener() {
        binding.dateButton.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker = builder.build()
            picker.addOnPositiveButtonClickListener {
                // Log.d("DatePicker Activity", "Date String = ${picker.headerText}:: Date epoch value = ${it}")
                val pickedDate = Date(it)
                viewModel.newSubmissionDate.value = pickedDate
            }
            picker.show(requireActivity().supportFragmentManager, picker.toString())
        }
    }

    private fun onActivityToggled(activity: Activity) {
        viewModel.onActivityCheckboxToggled(activity)
    }

    companion object {
        const val TAG = "AddSubmissionFragment"
    }
}