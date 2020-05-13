package com.example.qstreak.network

import com.example.qstreak.BuildConfig
import com.example.qstreak.models.Activity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface QstreakApiService {

    @POST("submissions")
    suspend fun createSubmission(
        @Body createSubmissionRequest: CreateSubmissionRequest,
        @Header(AUTHORIZATION) uid: String
    ): SubmissionResponse

    @GET("destinations")
    suspend fun getActivities(@Header(AUTHORIZATION) uid: String): List<Activity>

    @GET("submissions/{date}")
    suspend fun getSubmission(
        @Path("date") date: String,
        @Header(AUTHORIZATION) uid: String
    ): SubmissionResponse

    @DELETE("submissions/{date}")
    suspend fun deleteSubmission(
        @Path("date") date: String,
        @Header(AUTHORIZATION) uid: String
    ): Response<Unit>

    @PUT("submissions/{date}")
    suspend fun updateSubmission(
        @Path("date") date: String,
        @Body updateSubmissionRequest: UpdateSubmissionRequest,
        @Header(AUTHORIZATION) uid: String
    ): SubmissionResponse

    @PUT("account/update")
    suspend fun updateUser(
        @Body updateUserRequest: UpdateUserRequest,
        @Header(AUTHORIZATION) uid: String
    ): UpdateUserResponse

    @GET("dashboard")
    suspend fun getDashboardData(@Header(AUTHORIZATION) uid: String): DashboardResponse

    companion object {
        const val AUTHORIZATION = "Authorization"

        fun getQstreakApiService(): QstreakApiService {
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

            val client = builder.build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(QstreakApiService::class.java)
        }
    }
}
