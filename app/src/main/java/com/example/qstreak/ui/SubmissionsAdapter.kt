package com.example.qstreak.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qstreak.R
import com.example.qstreak.models.SubmissionWithActivities
import kotlinx.android.synthetic.main.submission_item.view.*

class SubmissionsAdapter(private val onItemClicked: (SubmissionWithActivities) -> Unit) :
    RecyclerView.Adapter<SubmissionsAdapter.SubmissionViewHolder>() {
    val submissionsWithActivities = mutableListOf<SubmissionWithActivities>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubmissionViewHolder {
        val holder = SubmissionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.submission_item,
                parent,
                false
            )
        )

        holder.itemView.setOnClickListener {
            onItemClicked(submissionsWithActivities[holder.adapterPosition])
        }

        return holder
    }

    override fun getItemCount(): Int {
        return submissionsWithActivities.size
    }

    override fun onBindViewHolder(holder: SubmissionViewHolder, position: Int) {
        val submissionActivitiesPair = submissionsWithActivities[position]
        holder.date.text = submissionActivitiesPair.submission.date
        holder.contactCount.text = submissionActivitiesPair.submission.contactCount.toString()
    }

    inner class SubmissionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.submission_date
        val contactCount: TextView = view.submission_contact_count
    }

    internal fun setSubmissions(submissionWiths: List<SubmissionWithActivities>) {
        this.submissionsWithActivities.clear()
        this.submissionsWithActivities.addAll(submissionWiths)
        notifyDataSetChanged()
    }
}
