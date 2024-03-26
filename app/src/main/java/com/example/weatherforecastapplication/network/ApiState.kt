package com.example.weatherforecastapplication.network

import com.example.weatherforecastapplication.model2.Responce
import com.example.weatherforecastapplication.model2.WeatherData


sealed class ApiState {
    class Sucess(val data:List<Responce>):ApiState()
    class Sucessed(val data:Responce):ApiState()
    class SucessedWeather(val data:WeatherData):ApiState()
    class SucessWeatherData(val data:List<WeatherData>):ApiState()
    class fail(val msg:Throwable):ApiState()
    object loading:ApiState()
}