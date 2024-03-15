package com.example.weatherforecastapplication.model2

data class Responce(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Listt>,
    val message: Int
)