package com.example.qstreak.ui

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qstreak.R
import com.example.qstreak.models.DailyLogItemInfo
import kotlinx.android.synthetic.main.daily_log_item.view.*

class DailyLogAdapter(
    private val onItemClicked: (DailyLogItemInfo) -> Unit
) :
    RecyclerView.Adapter<DailyLogAdapter.DailyLogViewHolder>() {

    private val infos = arrayListOf<DailyLogItemInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyLogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.daily_log_item,
            parent,
            false
        )

        // Set item to 1/7 of screen width so a week's worth of days are always shown.
        val layoutParams = view.layoutParams
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        layoutParams.width = (screenWidth / 7)
        view.layoutParams = layoutParams

        return DailyLogViewHolder(view)
    }

    override fun getItemCount(): Int {
        return infos.size
    }

    override fun onBindViewHolder(holder: DailyLogViewHolder, position: Int) {
        val itemInfo = infos[position]
        if (itemInfo.isTodayOrEarlier) {
            holder.itemView.setOnClickListener {
                this.onItemClicked(itemInfo)
            }
        }
        holder.dayOfWeek.text = itemInfo.dayOfWeek
        holder.dayOfMonth.text = itemInfo.dayOfMonth
        holder.checkImage.setImageResource(getResourceForCircle(itemInfo))
    }

    private fun getResourceForCircle(itemInfo: DailyLogItemInfo): Int {
        return when {
            itemInfo.isComplete -> {
                R.drawable.ic_green_check_filled
            }
            itemInfo.isTodayOrEarlier -> {
                R.drawable.dotted_check
            }
            else -> {
                R.drawable.dotted_circle
            }
        }
    }

    inner class DailyLogViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val dayOfWeek: TextView = view.day_of_week
        val dayOfMonth: TextView = view.day_of_month
        val checkImage: ImageView = view.check_image
    }

    internal fun setItemInfos(infos: List<DailyLogItemInfo>) {
        this.infos.clear()
        this.infos.addAll(infos)
        notifyDataSetChanged()
    }
}
