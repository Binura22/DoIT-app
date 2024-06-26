package com.example.doit.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DoIT::class], version = 1)
abstract class DoITDatabase : RoomDatabase() {

    abstract fun getDoITDao(): DoITDao

    companion object {
        @Volatile
        private var INSTANCE: DoITDatabase? = null

        fun getInstance(context: Context): DoITDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DoITDatabase::class.java,
                    "doit2_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}