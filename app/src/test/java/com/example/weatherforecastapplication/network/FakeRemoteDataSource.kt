package com.example.weatherforecastapplication.network

import com.example.weatherforecastapplication.model2.Responce
import com.example.weatherforecastapplication.model2.WeatherData

class FakeRemoteDataSource (var response: Responce
,
                           var weathers: WeatherData
) : RemoteDataSource {
    override suspend fun getWeatherOverNetwork(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        lang: String,
        units: String
    ): Responce {
        return response ?: throw IllegalStateException("Response list is empty")

          }

    override suspend fun getWeatherWithCityOverNetwork(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        units: String,
        Lang:String
    ): WeatherData {
        return weathers ?: throw IllegalStateException("Weather data list is empty")

    }

    override suspend fun getWeatherWithCity(city: String, apiKey: String ,
                                            units: String,
                                            Lang:String): WeatherData {
        return weathers ?: throw IllegalStateException("Weather data list is empty")
      //  return weathers
    }
}