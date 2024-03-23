package com.example.weatherforecastapplication.dp
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapplication.model2.Responce

@Dao
interface ResponceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponce(responce: Responce)

    @Query("SELECT * FROM response_table")
    fun getAllResponces(): List<Responce>
}