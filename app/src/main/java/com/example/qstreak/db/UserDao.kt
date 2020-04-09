package com.example.qstreak.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.qstreak.models.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: User)

    // could insert and update get the user automatically, since there will only be one?

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): User?

    @Update
    suspend fun update(user: User)
}