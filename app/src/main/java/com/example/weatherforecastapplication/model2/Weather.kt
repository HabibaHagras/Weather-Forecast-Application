package com.example.weatherforecastapplication.model2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")

data class Weather(

    val description: String,
    val icon: String,
    @PrimaryKey
    val id: Int,
    val main: String
)