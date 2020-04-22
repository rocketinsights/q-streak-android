package com.example.qstreak.network

import com.example.qstreak.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface QstreakApiSignupService {

    @POST("signup")
    suspend fun signup(
        @Body createUserRequest: CreateUserRequest
    ): CreateUserResponse

    companion object {
        fun getQstreakApiSignupService(): QstreakApiSignupService {
            val builder = OkHttpClient.Builder()

            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

            val client = builder.build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(QstreakApiSignupService::class.java)
        }
    }
}