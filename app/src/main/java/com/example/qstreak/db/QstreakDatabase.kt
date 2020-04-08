package com.example.qstreak.db

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.qstreak.models.Submission

@Database(entities = [Submission::class], version = 1)
abstract class QstreakDatabase : RoomDatabase() {

    abstract fun submissionDao(): SubmissionDao
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
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}