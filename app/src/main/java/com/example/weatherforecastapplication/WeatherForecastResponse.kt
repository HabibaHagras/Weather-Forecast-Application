package com.example.weatherforecastapplication
data class WeatherForecastResponse(
    val list: List<ForecastEntry>
)

data class ForecastEntry(
    val dt_txt: String,
    val main: Main,
    val weather: List<Weather>
)
