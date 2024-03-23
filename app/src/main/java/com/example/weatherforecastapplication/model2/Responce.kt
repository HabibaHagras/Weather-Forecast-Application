package com.example.weatherforecastapplication.model2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "response_table")
data class Responce(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0 ,
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Listt>,
    val message: Int
)