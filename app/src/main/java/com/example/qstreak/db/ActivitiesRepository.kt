package com.example.qstreak.db

import androidx.lifecycle.LiveData
import com.example.qstreak.models.Activity
import com.example.qstreak.network.QstreakApiService

class ActivitiesRepository(
    private val activitiesDao: ActivitiesDao
) {
    val activities: LiveData<List<Activity>> = activitiesDao.getAllActivities()

    suspend fun refreshActivities(uid: String) {
        activitiesDao.insertAllActivities(QstreakApiService.getQstreakApiService(uid).getActivities())
    }
}