package com.example.weatherforecastapplication.model2

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeRepo (var weather : MutableList<WeatherData> = mutableListOf(),
                var weatherResponce : MutableList<Responce> = mutableListOf()) :Repository{
   private val  _weather:Responce =  Responce(0,City(Coord(30.7914776,30.9957296),"EG",
                        347497,"Tanta",15000,1711338799,1711383021,7200),40,"200"
                        ,listOf(
                            Listt(
                                Clouds(0),
                                0,
                                "dt_txt",
                                Main(0.0, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0),
                                0.0,
                                Rain(0.0),
                                Sys("pod"),
                                0,
                                listOf(Weather("description", "icon", 0, "main")),
                                Wind(0, 0.0, 0.0)
                            )
                        ),0

                    )
    var weatherAlert : MutableList<Alarm> = mutableListOf()
    override suspend fun getAllWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        lang: String,
        units: String
    ): Flow<Responce> =
            flow<Responce> {
               emit(_weather)
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

    override  fun getStored(): Flow<List<WeatherData>> = flow{
        emit(weather)
    }

    override suspend fun insertWeatherData(product: WeatherData) {
        weather.add(product)
    }

    override  fun getStoredHome(): Flow<List<Responce>> = flow {
        emit(weatherResponce)

    }

    override suspend fun insertResponceData(weather: Responce) {
        weatherResponce.add(weather)
    }

    override suspend fun deletetWeatherData(weatherdata: WeatherData) {
        weather.remove(weatherdata)    }

    override  fun getStoredAlarms(): Flow<List<Alarm>> =flow {
        emit(weatherAlert)

    }

    override suspend fun insertAlarms(alarm: Alarm) {
        weatherAlert.add(alarm)
    }

    override suspend fun deleteAlarms(alarm: Alarm) {
        weatherAlert.remove(alarm)
    }
}