package com.example.weatherforecastapplication.dp

import androidx.room.TypeConverter
import com.example.weatherforecastapplication.model.Main
import com.example.weatherforecastapplication.model2.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherListConverter {
    @TypeConverter
    fun fromJson(value: String): List<Weather> {
        val listType = object : TypeToken<List<Weather>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toJson(list: List<Weather>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromMain(main: Main): String {
        return Gson().toJson(main)
    }

    @TypeConverter
    fun toMain(mainString: String): Main {
        return Gson().fromJson(mainString, Main::class.java)
    }
}
