//package com.example.weatherforecastapplication.dp
//
//import androidx.room.Dao;
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import androidx.room.Update
//import com.example.weatherforecastapplication.model2.Weather
//import com.example.weatherforecastapplication.model2.WeatherData
//@Dao
//public abstract interface WeatherDAO {
//
//    @Query("SELECT * FROM weather_table")
//    fun getAllWeather(): List<Weather>
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun insertWeather(product: Weather): Long
//    @Update
//    fun updateWeather(product: Weather)
//    @Delete
//    fun deleteWeather(product: Weather): Int
//}