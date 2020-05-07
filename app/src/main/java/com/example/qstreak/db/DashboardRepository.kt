package com.example.qstreak.db

import com.example.qstreak.network.ApiResult
import com.example.qstreak.network.DashboardResponse
import com.example.qstreak.network.QstreakApiService
import com.example.qstreak.network.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit

class DashboardRepository(
    private val api: QstreakApiService,
    private val retrofit: Retrofit,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun getDashboardContent(uid: String): ApiResult<DashboardResponse> {
        return safeApiCall(dispatcher, retrofit) {
            api.getDashboardData(uid)
        }
    }
}
