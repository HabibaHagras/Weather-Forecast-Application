package com.example.weatherforecastapplication.model

import kotlin.collections.List

//data class WeatherForecastResponse(
//   // val list: List<ForecastEntry>
//)

data class ForecastEntry(
//    val dt_txt: String,
//    val main: Main,
//    val weather: List<Weather>
    var cod     : String,
   var message : Int?          ,
  var cnt     : Int?            ,
    var list    : List<com.example.weatherforecastapplication.model.List>
//    var city    : City
)
