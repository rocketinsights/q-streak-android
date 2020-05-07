package com.example.qstreak.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qstreak.R
import com.example.qstreak.databinding.FragmentAddEditSubmissionBinding
import com.example.qstreak.models.Activity
import com.example.qstreak.utils.DateUtils
import com.example.qstreak.viewmodels.AddEditSubmissionViewModel
import com.example.qstreak.viewmodels.SubmissionsViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.help_card.view.*
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class AddEditSubmissionFragment : Fragment() {

    private val addEditViewModel: AddEditSubmissionViewModel by currentScope.viewModel(this)
    private val submissionsViewModel: SubmissionsViewModel by lazy {
        (requireActivity() as MainActivity).submissionsViewModel
    }

    private lateinit var binding: FragmentAddEditSubmissionBinding

    override fun onResume() {
        super.onResume()
        addEditViewModel.refreshActivities()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.getString(DATE_STRING)?.let {
            addEditViewModel.loadDate(it)
        }

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_add_edit_submission,
            null,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = addEditViewModel

        setupActivitiesList()
        observeCompletion()
        observeErrors()
        observeSelectedDateChange()
        setDateClickListener()
        helpButtonClickListener()

        return binding.root
    }

    private fun onSubmissionCompleted() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun setupActivitiesList() {
        val adapter = ActivitiesChecklistAdapter(
            this::onActivityToggled,
            addEditViewModel.activities.value.orEmpty()
        )

        // Set list of possible activities to select from
        addEditViewModel.activities.observe(viewLifecycleOwner, Observer {
            adapter.setActivities(it.orEmpty())
        })

        // Update UI with activities currently checked
        addEditViewModel.checkedActivities.observe(viewLifecycleOwner, Observer {
            adapter.setCheckedActivities(it)
        })

        binding.activitiesChecklist.adapter = adapter
        binding.activitiesChecklist.layoutManager = LinearLayoutManager(activity)
    }

    private fun observeCompletion() {
        addEditViewModel.submissionComplete.observe(viewLifecycleOwner, Observer {
            if (it) {
                onSubmissionCompleted()
            }
        })
    }

    private fun observeErrors() {
        addEditViewModel.errorToDisplay.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    private fun observeSelectedDateChange() {
        // TODO not proper MVVM?
        addEditViewModel.selectedDateString.observe(viewLifecycleOwner, Observer {
            submissionsViewModel.selectDate(it)
        })
    }

    private fun setDateClickListener() {
        binding.dateButton.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker = builder.build()
            picker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance().apply {
                    this.time = Date(it)
                    // TODO Don't know why we have to add one to the result of the picker
                    this.add(Calendar.DATE, 1)
                }
                val pickedDate = DateUtils.dateStringFormat.format(calendar.time)
                addEditViewModel.loadDate(pickedDate)
            }
            picker.show(requireActivity().supportFragmentManager, picker.toString())
        }
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

            window.contentView.help_header.text = getString(R.string.close_contact_header)
            window.contentView.help_text.text = getString(R.string.close_contact_text)

            val closeWindow = view.findViewById<ExtendedFloatingActionButton>(R.id.close_window)
            closeWindow.setOnClickListener {
                window.dismiss()
            }
        }
    }

    private fun onActivityToggled(activity: Activity) {
        addEditViewModel.onActivityCheckboxToggled(activity)
    }

    companion object {
        const val TAG = "AddSubmissionFragment"
        private const val DATE_STRING = "date_string"

        fun newInstance(dateString: String): AddEditSubmissionFragment {
            val fragment = AddEditSubmissionFragment()
            fragment.arguments = Bundle().apply { this.putString(DATE_STRING, dateString) }
            return fragment
        }
    }
}
