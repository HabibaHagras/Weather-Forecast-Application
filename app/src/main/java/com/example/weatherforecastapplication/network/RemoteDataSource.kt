package com.example.weatherforecastapplication.network

import com.example.weatherforecastapplication.model.WeatherData
import com.example.weatherforecastapplication.model2.Responce

interface RemoteDataSource {
    suspend fun getWeatherOverNetwork(latitude: Double,
                                     longitude: Double,
                                      apiKey: String,
                                   units: String ): Responce
    suspend fun getWeatherWithCityOverNetwork(latitude: Double,
                                              longitude: Double,
                                              apiKey: String,
                                              units: String ):WeatherData
}