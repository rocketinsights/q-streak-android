package com.example.qstreak.db

import com.example.qstreak.models.User
import com.example.qstreak.network.*
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit

class UserRepository(
    private val userDao: UserDao,
    private val api: QstreakApiService,
    private val signupApi: QstreakApiSignupService,
    private val retrofit: Retrofit,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun getUser(): User? = userDao.getUser()

    suspend fun createUser(
        name: String?,
        zip: String
    ): ApiResult<CreateUserResponse> {
        val apiResponse = safeApiCall(dispatcher, retrofit) {
            signupApi.signup(CreateUserRequest(Account(name, zip)))
        }

        // Add user to local database if API reports user successfully created.
        if (apiResponse is ApiResult.Success) {
            val user = apiResponse.data.toUserModel()
            userDao.insert(user)
        }

        return apiResponse
    }

    suspend fun updateUser(
        name: String?,
        zip: String,
        uid: String
    ): ApiResult<UpdateUserResponse> {
        val apiResponse = safeApiCall(dispatcher, retrofit) {
            api.updateUser(UpdateUserRequest(UpdateUserData(name, zip)), uid)
        }

        // Update user in local database if API reports user successfully updated.
        if (apiResponse is ApiResult.Success) {
            val user = apiResponse.data.toUserModel()
            userDao.update(user)
        }

        return apiResponse
    }
}