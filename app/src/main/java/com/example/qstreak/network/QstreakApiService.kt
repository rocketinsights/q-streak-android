package com.example.qstreak.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface QstreakApiService {

    @POST("submissions")
    suspend fun createSubmission(
        @Body createSubmissionRequest: CreateSubmissionRequest
    ): CreateSubmissionResponse

    companion object {
        // TODO: appropriate way to connect api service to value of bearer token
        fun getQstreakApiService(uid: String): QstreakApiService {
            val builder = OkHttpClient.Builder()

            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            builder.addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request().newBuilder().header("Authorization",
                        "Bearer $uid"
                    ).build()

                    return chain.proceed(request)
                }
            })

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