package com.example.myapplication

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [CarPassEvent::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carPassDao(): CarPassDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "peaje.db"
                ).build().also { INSTANCE = it }
            }
    }
}