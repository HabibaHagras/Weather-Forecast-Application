package com.example.weatherforecastapplication.dp
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapplication.model2.Responce
import kotlinx.coroutines.flow.Flow

@Dao
interface ResponceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertResponce(responce: Responce)

    @Query("SELECT * FROM response_table")
     fun getAllResponces(): Flow<List<Responce>>
}