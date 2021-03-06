package com.example.qstreak.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    val dateStringFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val dateStringFormatForAddEditRecord = SimpleDateFormat("EEEE MMMM d", Locale.US)
    val dateFormatDayOfWeek = SimpleDateFormat("E", Locale.US)
    val dateFormatDayOfMonth = SimpleDateFormat("dd", Locale.US)
    val dateFormatWeekOfDate = SimpleDateFormat("MMMM d", Locale.US)

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

    fun getWeekOfDateString(date: Date): String {
        val calendar = Calendar.getInstance().apply { this.time = date }
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.add(Calendar.DATE, -(dayOfWeek - 1))

        return dateFormatWeekOfDate.format(calendar.time)
    }

    fun getDateDisplayStringFromDate(date: Date = Calendar.getInstance().time): String {
        return dateStringFormatForAddEditRecord.format(date)
    }

    fun getDateDisplayStringFromDbRecord(dbString: String): String? {
        return getDateFromDbRecord(dbString)?.let {
            dateStringFormatForAddEditRecord.format(it)
        }
    }

    fun getDateFromDbRecord(dbString: String): Date? {
        return dateStringFormat.parse(dbString)
    }
}



