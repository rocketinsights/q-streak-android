package com.example.qstreak.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qstreak.R
import com.example.qstreak.databinding.FragmentAddEditSubmissionBinding
import com.example.qstreak.databinding.FragmentEditProfileBinding
import com.example.qstreak.viewmodels.AddEditSubmissionViewModel
import com.example.qstreak.viewmodels.EditProfileViewModel
import com.example.qstreak.viewmodels.SubmissionsViewModel
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProfileFragment : Fragment() {

    private val editProfileViewModel: EditProfileViewModel by currentScope.viewModel(this)

    private lateinit var binding: FragmentEditProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_edit_profile,
            null,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = editProfileViewModel

        setupBackButtonClickListeners()

        return binding.root
    }


    private fun setupBackButtonClickListeners() {
        binding.backButton.setOnClickListener {
            (requireActivity() as MainActivity).onBackPressed()
        }
        binding.doneButton.setOnClickListener {
            (requireActivity() as MainActivity).onBackPressed()
        }
    }

    companion object {
        const val TAG = "EditProfileFragment"

        fun newInstance(): EditProfileFragment {
            return EditProfileFragment()
        }
    }
}
