package com.example.weatherforecastapplication.model2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    val lat: Double,
    val log: Double,
    val hour: Int,
    val minute: Int,
    val mounth: Int,
    val year: Int,
    val day: Int,
    @PrimaryKey
    val reqest_code:Int
)
