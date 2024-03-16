package com.example.weatherforecastapplication.model

import com.example.weatherforecastapplication.model2.Weather
import kotlin.collections.List

data class List (
    var main       : Main,
    var weather    : List<Weather>,
    var dt_txt      : String
)