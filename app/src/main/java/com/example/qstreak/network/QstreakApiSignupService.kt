package com.example.qstreak.network

import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

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
