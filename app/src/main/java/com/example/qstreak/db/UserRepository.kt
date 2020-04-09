package com.example.qstreak.db

import androidx.lifecycle.LiveData
import com.example.qstreak.models.User

class UserRepository(val userDao: UserDao) {
    suspend fun getUser(): User? = userDao.getUser()

    suspend fun insert(user: User){
        userDao.insert(user)
    }

    suspend fun update(user: User) {
        userDao.update(user)
    }

    // maybe proxy getUser too? or just return user val?
}