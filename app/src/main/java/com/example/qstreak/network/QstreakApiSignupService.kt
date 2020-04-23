package com.example.qstreak.network

import com.example.qstreak.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import timber.log.Timber
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

interface QstreakApiSignupService {

    @POST("signup")
    suspend fun signup(
        @Body createUserRequest: CreateUserRequest
    ): CreateUserResponse

    companion object {
        fun getQstreakApiSignupService(retrofit: Retrofit): QstreakApiSignupService {
            return retrofit.create(QstreakApiSignupService::class.java)
        }
    }
}
