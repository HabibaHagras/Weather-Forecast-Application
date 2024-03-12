package com.example.weatherforecastapplication.model

data class Weather(
    val description: String,
var id          : Int,
var main        : String,
    val icon: String
)