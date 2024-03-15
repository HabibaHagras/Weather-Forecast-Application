package com.example.weatherforecastapplication.model

import com.example.weatherforecastapplication.model2.Responce

interface Repository {
    suspend fun getAllWeather(latitude: Double,
                              longitude: Double,
                              apiKey: String,
                              units: String ): Responce
    suspend fun getWeatherWithCity(latitude: Double,
                                   longitude: Double,
                                   apiKey: String,
                                   units: String):WeatherData
    suspend fun getWeatherWithCity2(city: String,apiKey:String):WeatherData

}