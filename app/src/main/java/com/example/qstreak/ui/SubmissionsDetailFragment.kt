package com.example.qstreak.ui

import android.os.Bundle
import androidx.fragment.app.Fragment

class SubmissionsDetailFragment : Fragment() {

    companion object {

        fun newInstance(submissionId: Int): SubmissionsDetailFragment {
            val args = Bundle()
            args.putInt("submissionId", submissionId)
            return SubmissionsDetailFragment().apply { this.arguments = args }
        }
    }

}