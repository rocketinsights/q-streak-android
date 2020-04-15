package com.example.qstreak.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class SubmissionActivitiesPair(
    @Embedded
    var submission: Submission,
    @Relation(
        parentColumn = "submission_id",
        entity = Activity::class,
        entityColumn = "activity_slug",
        associateBy = Junction(
            value = SubmissionWithActivity::class,
            parentColumn = "submission_id",
            entityColumn = "activity_slug"
        )
    )
    var activities: List<Activity>
)