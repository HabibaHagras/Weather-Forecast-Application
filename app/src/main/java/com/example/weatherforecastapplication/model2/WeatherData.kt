package com.example.weatherforecastapplication.model2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weatherData_table")

data class WeatherData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: CoordWeather,
    val dt: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)