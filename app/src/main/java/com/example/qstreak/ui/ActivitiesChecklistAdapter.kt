package com.example.qstreak.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qstreak.R
import com.example.qstreak.models.Activity
import kotlinx.android.synthetic.main.submission_activity_item.view.*

class ActivitiesChecklistAdapter(
    private val onItemClicked: (Activity) -> Unit,
    activities: List<Activity>
) :
    RecyclerView.Adapter<ActivitiesChecklistAdapter.ActivityViewHolder>() {

    private val activities = mutableListOf<Activity>().apply {
        this.addAll(activities)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val holder = ActivityViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.submission_activity_item,
                parent,
                false
            )
        )

        holder.itemView.setOnClickListener {
            holder.checkBox.toggle()
            onItemClicked(activities[holder.adapterPosition])
        }

        return holder
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        holder.activityName.text = activity.name
    }

    inner class ActivityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.checkbox
        val activityName: TextView = view.activity_name
    }

    internal fun setActivities(activities: List<Activity>) {
        this.activities.clear()
        this.activities.addAll(activities)
        notifyDataSetChanged()
    }
}
