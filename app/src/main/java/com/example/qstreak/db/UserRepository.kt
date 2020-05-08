package com.example.qstreak.db

import com.example.qstreak.models.User
import com.example.qstreak.network.*
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit

class UserRepository(
    private val userDao: UserDao,
    private val api: QstreakApiSignupService,
    private val retrofit: Retrofit,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun getUser(): User? = userDao.getUser()

    suspend fun createUser(
        name: String?,
        zip: String
    ): ApiResult<CreateUserResponse> {
        val apiResponse = safeApiCall(dispatcher, retrofit) {
            api.signup(CreateUserRequest(Account(name, zip)))
        }

        // Add user to local database if API reports user successfully created.
        if (apiResponse is ApiResult.Success) {
            val user = apiResponse.data.toUserModel()
            userDao.insert(user)
        }

        return apiResponse
    }

    suspend fun update(user: User) {
        userDao.update(user)
    }

    // maybe proxy getUser too? or just return user val?
}
