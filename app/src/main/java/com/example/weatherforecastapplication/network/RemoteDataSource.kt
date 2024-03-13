package com.example.weatherforecastapplication.network

import com.example.weatherforecastapplication.model.ForecastEntry
import com.example.weatherforecastapplication.model.WeatherData

interface RemoteDataSource {
    suspend fun getWeatherOverNetwork(latitude: Double,
                                     longitude: Double,
                                      apiKey: String,
                                   units: String ):ForecastEntry
    suspend fun getWeatherWithCityOverNetwork(

                                              city: String,   apiKey: String ):WeatherData
}