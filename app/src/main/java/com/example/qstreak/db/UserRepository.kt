package com.example.qstreak.db

import com.example.qstreak.models.User
import com.example.qstreak.network.Account
import com.example.qstreak.network.CreateUserRequest
import com.example.qstreak.network.QstreakApiSignupService

class UserRepository(val userDao: UserDao) {
    suspend fun getUser(): User? = userDao.getUser()

    suspend fun createUser(age: Int, householdSize: Int, zip: String): User {
        val api = QstreakApiSignupService.getQstreakApiSignupService()
        val response = api.signup(CreateUserRequest(Account(age, householdSize, zip)))
        // TODO add type converter to Response object
        val newUser =
            User(response.zip, response.age, response.householdSize, response.uid)
        userDao.insert(newUser)

        return newUser
    }

    suspend fun update(user: User) {
        userDao.update(user)
    }

    // maybe proxy getUser too? or just return user val?
}