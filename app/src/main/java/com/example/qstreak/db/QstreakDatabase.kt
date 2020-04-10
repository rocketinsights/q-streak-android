package com.example.qstreak.db

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.qstreak.models.Submission
import com.example.qstreak.models.User

@Database(entities = [Submission::class, User::class], version = 3)
abstract class QstreakDatabase : RoomDatabase() {

    abstract fun submissionDao(): SubmissionDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: QstreakDatabase? = null
        fun getInstance(context: Context): QstreakDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(QstreakDatabase::class) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QstreakDatabase::class.java,
                    "qstreak_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}