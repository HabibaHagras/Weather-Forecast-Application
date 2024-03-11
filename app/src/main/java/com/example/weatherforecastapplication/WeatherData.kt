package com.example.weatherforecastapplication

data class WeatherData(
    val name: String,
    val main: Main,
    val weather: List<Weather>
)
