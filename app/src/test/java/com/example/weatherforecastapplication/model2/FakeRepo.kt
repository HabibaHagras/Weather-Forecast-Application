package com.example.weatherforecastapplication.model2

import kotlinx.coroutines.flow.Flow

class FakeRepo  :Repository{
    override suspend fun getAllWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        lang: String,
        units: String
    ): Flow<Responce> {
        TODO("Not yet implemented")
    }

    override suspend fun getWeatherWithCity(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        units: String
    ): Flow<WeatherData> {
        TODO("Not yet implemented")
    }

    override suspend fun getWeatherWithCity2(city: String, apiKey: String): Flow<WeatherData>  {
        TODO("Not yet implemented")
    }

    override suspend fun getStored(): Flow<List<WeatherData>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertWeatherData(product: WeatherData) {
        TODO("Not yet implemented")
    }

    override suspend fun getStoredHome(): Flow<List<Responce>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertResponceData(weather: Responce) {
        TODO("Not yet implemented")
    }

    override suspend fun deletetWeatherData(weather: WeatherData) {
        TODO("Not yet implemented")
    }
}