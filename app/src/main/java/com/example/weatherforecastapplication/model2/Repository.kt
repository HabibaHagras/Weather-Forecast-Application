package com.example.weatherforecastapplication.model2

import androidx.lifecycle.LiveData
import com.example.weatherforecastapplication.model2.Responce
import com.example.weatherforecastapplication.model2.WeatherData
import kotlin.collections.List

interface Repository {
    suspend fun getAllWeather(latitude: Double,
                              longitude: Double,
                              apiKey: String,
                              lang: String,
                              units: String ): Responce
    suspend fun getWeatherWithCity(latitude: Double,
                                   longitude: Double,
                                   apiKey: String,
                                   units: String): WeatherData
    suspend fun getWeatherWithCity2(city: String,apiKey:String): WeatherData
    suspend fun getStored(): List<WeatherData>
    suspend fun insertWeatherData(product: WeatherData)
    suspend fun getStoredHome():List<Responce>
    suspend fun insertResponceData(weather: Responce)


}