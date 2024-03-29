package com.example.weatherforecastapplication.dp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapplication.model2.Alarm
import com.example.weatherforecastapplication.model2.Responce
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAlarm(alarm: Alarm)

    @Query("SELECT * FROM alarms")
     fun getAllAlarms(): Flow<List<Alarm>>
    @Delete
    fun deleteAlarm(alarm: Alarm)
}