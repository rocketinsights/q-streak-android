package com.example.qstreak.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    val dateStringFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val dateFormatDayOfWeek = SimpleDateFormat("E", Locale.US)
    val dateFormatDayOfMonth = SimpleDateFormat("dd", Locale.US)

    fun listOfThisAndLastWeekDates(): List<Date> {
        val calendar = Calendar.getInstance()
        val diff = calendar.get(Calendar.DAY_OF_WEEK) - calendar.firstDayOfWeek
        val daysToSubtractForStartDate = diff + 7

        // set calendar to start of last week
        calendar.add(
            Calendar.DATE,
            -daysToSubtractForStartDate
        )

        return mutableListOf<Date>().apply {
            for (i in 1..14) {
                this.add(calendar.time)
                calendar.add(Calendar.DATE, 1)
            }
        }
    }
}



