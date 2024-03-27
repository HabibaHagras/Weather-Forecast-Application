package com.example.weatherforecastapplication.view.favorite_view

import com.example.weatherforecastapplication.model2.WeatherData

interface FavListener {
    fun  OnCLickIteamFav(lat: Double ,lon:Double,city:String)
    fun deleteIteamFav(weather: WeatherData)
}