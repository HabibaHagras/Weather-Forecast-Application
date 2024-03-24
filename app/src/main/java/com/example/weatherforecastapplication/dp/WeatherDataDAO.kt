package com.example.weatherforecastapplication.dp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weatherforecastapplication.model2.Weather
import com.example.weatherforecastapplication.model2.WeatherData
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDataDAO {
    @Query("SELECT * FROM weatherData_table")
     fun getAll(): Flow<List<WeatherData>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(product: WeatherData): Long
    @Update
    fun update(product: WeatherData)

    @Query("SELECT * FROM weather_table WHERE id = :parentId")
     fun getWeatherByParentId(parentId: Int): Flow<List<Weather>>
    @Delete
    fun delete(product: WeatherData): Int
}