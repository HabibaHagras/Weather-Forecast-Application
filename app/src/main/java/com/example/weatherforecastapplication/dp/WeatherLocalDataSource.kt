package com.example.weatherforecastapplication.dp

import androidx.lifecycle.LiveData
import com.example.weatherforecastapplication.model2.Responce
import com.example.weatherforecastapplication.model2.Weather
import com.example.weatherforecastapplication.model2.WeatherData
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    suspend fun insertWeatherData(product: WeatherData)
    suspend fun deleteWeatherData(product: WeatherData)
    suspend fun getStoredProducts(): Flow<List<WeatherData>>
    suspend fun insertWeatherHome(weather: Responce)
    suspend fun getStoredWeatherHome(): Flow<List<Responce>>



}