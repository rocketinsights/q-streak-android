package com.example.qstreak.models

import com.example.qstreak.utils.DateUtils.dateFormatDayOfMonth
import com.example.qstreak.utils.DateUtils.dateFormatDayOfWeek
import java.text.SimpleDateFormat
import java.util.*

data class DailyLogItemInfo(
    val date: Date,
    val submission: SubmissionWithActivities? = null
) {
    private val dateFormatFullDay = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val dayOfWeek: String = dateFormatDayOfWeek.format(date).toUpperCase(Locale.US)
    val dayOfMonth: String = dateFormatDayOfMonth.format(date)

    // For use in UI conditional logic
    val isComplete = submission != null
    val isToday =
        dateFormatFullDay.format(date) == dateFormatFullDay.format(Calendar.getInstance().time)
}
