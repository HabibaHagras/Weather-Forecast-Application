package com.example.weatherforecastapplication.dp

import com.example.weatherforecastapplication.model2.Responce
import com.example.weatherforecastapplication.model2.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeLocalDataSource (var weather : MutableList<WeatherData> = mutableListOf(),
                           var weatherResponce : MutableList<Responce> = mutableListOf()
    ) :WeatherLocalDataSource {

    override suspend fun insertWeatherData(product: WeatherData) {
        weather.add(product)
    }

    override suspend fun deleteWeatherData(product: WeatherData) {
        weather.remove(product)
    }

    override suspend fun getStoredProducts(): Flow<List<WeatherData>>  = weather.let {
        return@let flowOf(it)
    }

    override suspend fun insertWeatherHome(weather: Responce) {
        weatherResponce.add(weather)    }

    override suspend fun getStoredWeatherHome(): Flow<List<Responce>> =weatherResponce.let {
        return@let flowOf(it)
    }
    }