package com.example.weatherforecastapplication.network

import com.example.weatherforecastapplication.model.ForecastEntry

interface RemoteDataSource {
    suspend fun getWeatherOverNetwork(latitude: Double,
                                     longitude: Double,
                                      apiKey: String,
                                   units: String ):ForecastEntry

}