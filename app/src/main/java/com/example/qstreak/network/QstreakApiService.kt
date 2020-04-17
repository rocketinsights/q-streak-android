package com.example.qstreak.network

import com.example.qstreak.models.Activity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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

    @GET("submissions/{remoteId}")
    suspend fun getSubmission(
        @Path("remoteId") remoteId: Int,
        @Header(AUTHORIZATION) uid: String
    ): SubmissionResponse

    companion object {
        const val AUTHORIZATION = "Authorization"
        
        fun getQstreakApiService(): QstreakApiService {
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

            val client = builder.build()

            return Retrofit.Builder()
                .baseUrl("http://10.0.2.2:4000/api/")
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(QstreakApiService::class.java)
        }
    }
}
