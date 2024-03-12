package com.example.weatherforecastapplication.model

import kotlin.collections.List

data class List (
    var main       : Main,
    var weather    : List<Weather>,
    var dt_txt      : String
)