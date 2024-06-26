package com.example.weatherforecastapplication.dp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforecastapplication.model2.Alarm
import com.example.weatherforecastapplication.model2.Responce
import com.example.weatherforecastapplication.model2.Weather
import com.example.weatherforecastapplication.model2.WeatherData

@TypeConverters(WeatherListConverter::class)
@Database(entities = arrayOf( WeatherData::class, Responce::class ,Alarm::class), version = 1 )
abstract class db : RoomDatabase() {
    abstract fun getWeatherDataDao(): WeatherDataDAO
    abstract fun responceDao(): ResponceDao
    abstract fun AlarmDao(): AlarmDao

    companion object{
        @Volatile
        private var INSTANCE: db? = null
        fun getInstance (ctx: Context): db{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, db::class.java, "weather_database")
                    .build()
                INSTANCE = instance
                instance }
        }
    }
}