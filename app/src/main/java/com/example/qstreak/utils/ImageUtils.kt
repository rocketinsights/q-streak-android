package com.example.qstreak.utils

import com.example.qstreak.R

object ImageUtils {
    fun getImageByScore(score: Int?): Int {
        return when (score) {
            5 -> R.drawable.qstreak_score_5
            4 -> R.drawable.qstreak_score_4
            3 -> R.drawable.qstreak_score_3
            2 -> R.drawable.qstreak_score_2
            1 -> R.drawable.qstreak_score_1
            0 -> R.drawable.qstreak_score_0
            else -> 0
        }
    }

    fun getImageByLocation(activity: String?): Int {
        return when (activity) {
            "grocery-store" -> R.drawable.qstreak_longest_streak
            "post-office" -> R.drawable.qstreak_help_button
            "pharmacy" -> R.drawable.qstreak_risk_warning
            else -> 0
        }
    }
}
