package com.example.qstreak

import com.example.qstreak.models.Activity
import com.example.qstreak.models.Submission
import com.example.qstreak.models.SubmissionWithActivities

object TestFixtures {
    // User
    val uid = "uid"

    // Submissions
    val date1 = "2020-01-01"
    val submission1 = Submission(date1, 1, 1)
    val date2 = "2020-01-02"
    val submission2 = Submission("2020-01-02", 2, 2)
    val defaultSubmissions = listOf(submission1, submission2)
    val dateWithNoSubmission = "2020-01-03"

    // Activities
    val activity1 = Activity(1, "activity1", "slug1", "f111")
    val activity2 = Activity(2, "activity2", "slug2", "f222")
    val activity3 = Activity(3, "activity3", "slug3", "f333")
    val activities1 = listOf(activity1, activity2)
    val activities2 = listOf(activity2, activity3)

    val submissionWithActivities1 = SubmissionWithActivities(submission1, activities1)
    val submissionWithActivities2 = SubmissionWithActivities(submission2, activities2)
}
