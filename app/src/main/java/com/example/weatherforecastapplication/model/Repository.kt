package com.example.weatherforecastapplication.model

import com.example.weatherforecastapplication.model.ForecastEntry

interface Repository {
    suspend fun getAllWeather(latitude: Double,
                              longitude: Double,
                              apiKey: String,
                              units: String ):ForecastEntry
    suspend fun getWeatherWithCity(

        city: String,   apiKey: String ):WeatherData
}