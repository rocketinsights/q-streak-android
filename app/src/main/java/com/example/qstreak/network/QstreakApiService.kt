package com.example.qstreak.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface QstreakApiService {

    @POST("signup")
    suspend fun signup(
        @Body createUserRequest: CreateUserRequest
    ): CreateUserResponse

    companion object {
        fun getQstreakApiService(): QstreakApiService {
            val builder = OkHttpClient.Builder()

            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))

            val client = builder.build()

            return Retrofit.Builder()
                .baseUrl("https://localhost:4000/api/")
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(QstreakApiService::class.java)
        }
    }
}