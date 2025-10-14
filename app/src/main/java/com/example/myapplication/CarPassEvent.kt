package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entidad que representa un "evento de paso de auto"
@Entity(tableName = "car_pass_events")
data class CarPassEvent(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestampMillis: Long // momento exacto en que pasó el auto (ms desde 1970)
)