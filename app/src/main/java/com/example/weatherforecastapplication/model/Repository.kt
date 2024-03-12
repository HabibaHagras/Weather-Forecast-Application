package com.example.weatherforecastapplication.model

import com.example.weatherforecastapplication.model.ForecastEntry

interface Repository {
    suspend fun getAllWeather(latitude: Double,
                              longitude: Double,
                              apiKey: String,
                              units: String ):ForecastEntry
}