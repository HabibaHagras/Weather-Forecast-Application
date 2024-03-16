package com.example.weatherforecastapplication.dp

import androidx.room.TypeConverter
import com.example.weatherforecastapplication.model2.Main
import com.google.gson.Gson

class MainConverter {
    @TypeConverter
    fun fromMain(main: Main): String {
        return Gson().toJson(main)
    }

    @TypeConverter
    fun toMain(mainString: String): Main {
        return Gson().fromJson(mainString, Main::class.java)
    }
}

