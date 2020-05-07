package com.example.qstreak.models

import com.example.qstreak.utils.DateUtils.dateFormatDayOfMonth
import com.example.qstreak.utils.DateUtils.dateFormatDayOfWeek
import java.util.*

data class DailyLogItemInfo(
    val date: Date,
    val submission: SubmissionWithActivities? = null
) {
    val dayOfWeek: String = dateFormatDayOfWeek.format(date).toUpperCase(Locale.US)
    val dayOfMonth: String = dateFormatDayOfMonth.format(date)

    // For use in UI conditional logic
    val isComplete = submission != null
    val isTodayOrEarlier = date <= Calendar.getInstance().time
    val isToday = dayOfMonth == dateFormatDayOfMonth.format(Date())
}
