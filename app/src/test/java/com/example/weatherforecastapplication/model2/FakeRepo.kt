package com.example.weatherforecastapplication.model2

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeRepo (var weather : MutableList<WeatherData> = mutableListOf(),
                var weatherResponce : MutableList<Responce> = mutableListOf()) :Repository{
    override suspend fun getAllWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        lang: String,
        units: String
    ): Flow<Responce> =
            flow<Responce> {
                emit(weatherResponce.first())

            }


    override suspend fun getWeatherWithCity(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        units: String,
        lang: String
    ): Flow<WeatherData> = flow {
        weather.forEach { emit(it) }
    }

    override suspend fun getWeatherWithCity2(city: String, apiKey: String,   units: String,
                                             lang: String): Flow<WeatherData>  = flow {
        weather.forEach { emit(it) }
    }

    override suspend fun getStored(): Flow<List<WeatherData>> = flow{
        emit(weather)
    }

    override suspend fun insertWeatherData(product: WeatherData) {
        weather.add(product)
    }

    override suspend fun getStoredHome(): Flow<List<Responce>> = flow {
        emit(weatherResponce)
    }

    override suspend fun insertResponceData(weather: Responce) {
        weatherResponce.add(weather)
    }

    override suspend fun deletetWeatherData(weatherdata: WeatherData) {
        weather.remove(weatherdata)    }
}