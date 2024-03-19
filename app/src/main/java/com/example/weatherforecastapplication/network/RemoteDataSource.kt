package com.example.weatherforecastapplication.network

import com.example.weatherforecastapplication.model2.WeatherData
import com.example.weatherforecastapplication.model2.Responce

interface RemoteDataSource {
    suspend fun getWeatherOverNetwork(latitude: Double,
                                     longitude: Double,
                                      apiKey: String, lang: String,
                                   units: String ): Responce
    suspend fun getWeatherWithCityOverNetwork(latitude: Double,
                                              longitude: Double,
                                              apiKey: String,
                                              units: String ): WeatherData

    suspend fun getWeatherWithCity(city:String,apiKey: String): WeatherData
}