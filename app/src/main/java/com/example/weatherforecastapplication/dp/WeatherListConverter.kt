package com.example.weatherforecastapplication.dp

import androidx.room.TypeConverter
import com.example.weatherforecastapplication.model2.Alarm
import com.example.weatherforecastapplication.model2.City
import com.example.weatherforecastapplication.model2.Clouds
import com.example.weatherforecastapplication.model2.Coord
import com.example.weatherforecastapplication.model2.CoordWeather
import com.example.weatherforecastapplication.model2.Listt
import com.example.weatherforecastapplication.model2.Main
import com.example.weatherforecastapplication.model2.Responce
import com.example.weatherforecastapplication.model2.Sys
import com.example.weatherforecastapplication.model2.Weather
import com.example.weatherforecastapplication.model2.Wind
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
    fun fromResponceJson(responce: Responce): String {
        return Gson().toJson(responce)
    }
    @TypeConverter
    fun toResponceJson(responceString: String): Responce {
        return Gson().fromJson(responceString, Responce::class.java)
    }


    @TypeConverter
    fun fromMain(main: Main): String {
        return Gson().toJson(main)
    }

    @TypeConverter
    fun toMain(mainString: String): Main {
        return Gson().fromJson(mainString, Main::class.java)
    }

    @TypeConverter
    fun fromcloud(value: String): Clouds {
        return Gson().fromJson(value, Clouds::class.java)
    }

    @TypeConverter
    fun tocloud(clouds: Clouds): String {
        return Gson().toJson(clouds)
    }
    @TypeConverter
    fun fromCoord(value: String): CoordWeather {
        return Gson().fromJson(value, CoordWeather::class.java)
    }

    @TypeConverter
    fun toCoord(coord: CoordWeather): String {
        return Gson().toJson(coord)
    }
    @TypeConverter
    fun fromWind(value: String): Wind {
        return Gson().fromJson(value, Wind::class.java)
    }

    @TypeConverter
    fun toWind(wind: Wind): String {
        return Gson().toJson(wind)
    }
    @TypeConverter
    fun fromSys(sys: Sys): String {
        return Gson().toJson(sys)
    }

    @TypeConverter
    fun toSys(sysJson: String): Sys {
        return Gson().fromJson(sysJson, Sys::class.java)
    }
    @TypeConverter
    fun fromString(value: String): List<Listt> {
        val listType = object : TypeToken<List<Listt>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Listt>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
    @TypeConverter
    fun fromStringCity(value: String): City {
        return Gson().fromJson(value, City::class.java)
    }

    @TypeConverter
    fun cityToString(city: City): String {
        return Gson().toJson(city)
    }


    @TypeConverter
    fun fromAlarmJson(alarm: Alarm): String {
        return Gson().toJson(alarm)
    }
    @TypeConverter
    fun toAlarmJson(alarm: String): Alarm {
        return Gson().fromJson(alarm, Alarm::class.java)
    }


}
