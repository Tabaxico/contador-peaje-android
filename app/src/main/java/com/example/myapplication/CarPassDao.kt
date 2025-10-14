package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CarPassDao {
    @Insert
    suspend fun insert(event: CarPassEvent)

    @Query("SELECT COUNT(*) FROM car_pass_events WHERE timestampMillis BETWEEN :startMs AND :endMs")
    suspend fun countBetween(startMs: Long, endMs: Long): Int
}