package com.example.weatherforecastapplication.dp

import androidx.lifecycle.LiveData
import com.example.weatherforecastapplication.model2.Alarm
import com.example.weatherforecastapplication.model2.Responce
import com.example.weatherforecastapplication.model2.Weather
import com.example.weatherforecastapplication.model2.WeatherData
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    suspend fun insertWeatherData(product: WeatherData)
    suspend fun deleteWeatherData(product: WeatherData)
     fun getStoredProducts(): Flow<List<WeatherData>>
    suspend fun insertWeatherHome(weather: Responce)
     fun getStoredWeatherHome(): Flow<List<Responce>>
     fun getStoredAlarms(): Flow<List<Alarm>>
    suspend fun insertAlarms(alarm: Alarm)
    suspend fun deleteAlarms(alarm: Alarm)




}