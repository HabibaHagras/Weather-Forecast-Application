package com.example.weatherforecastapplication.dp

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weatherforecastapplication.model2.Weather
import com.example.weatherforecastapplication.model2.WeatherData

interface WeatherDataDAO {
    @Query("SELECT * FROM weatherData_table")
    fun getAll(): List<WeatherData>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(product: WeatherData): Long
    @Update
    fun update(product: WeatherData)

    @Query("SELECT * FROM weather_table WHERE id = :parentId")
    suspend fun getWeatherByParentId(parentId: Long): List<Weather>
    @Delete
    fun delete(product: WeatherData): Int
}