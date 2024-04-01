package com.example.weatherforecastapplication.model2

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherforecastapplication.dp.WeatherLocalDataSource
import com.example.weatherforecastapplication.network.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.collections.List

class RepositoryImp  constructor(
    private var RemoteDataSource: RemoteDataSource,private var LocalDataSource:WeatherLocalDataSource
): Repository {

    companion object{
        private var instance: RepositoryImp?=null
        fun getInstance( productRemoteDataSource: RemoteDataSource,
                         productLocalDataSource: WeatherLocalDataSource,        )
                : RepositoryImp {
            return instance ?: synchronized(this){
                val temp= RepositoryImp(productRemoteDataSource,productLocalDataSource)
                instance =temp
                temp
            }
        }
    }

    override suspend fun getAllWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        lang: String,
        units: String
    ):  Flow<Responce> {
        return  flowOf( RemoteDataSource.getWeatherOverNetwork(latitude,longitude,apiKey,lang,units)   ) }

    override  suspend fun getWeatherWithCity(latitude: Double,
                                            longitude: Double,
                                            apiKey: String,
                                            units: String,
                                            lang: String,
    ): Flow<WeatherData> {
return flowOf (RemoteDataSource.getWeatherWithCityOverNetwork(latitude,longitude,apiKey,units,lang)  ) }

    override  suspend fun getWeatherWithCity2(city: String, apiKey: String,units: String,
                                             lang: String,): Flow<WeatherData> {
      return  flowOf(RemoteDataSource.getWeatherWithCity(city,apiKey,units,lang))
    }

    override  fun getStored(): Flow<List<WeatherData>> {
        return LocalDataSource.getStoredProducts()
    }

    override suspend fun insertWeatherData(product: WeatherData) {
        return LocalDataSource.insertWeatherData(product)
    }

    override  fun getStoredHome(): Flow<List<Responce>> {
        return LocalDataSource.getStoredWeatherHome()
    }

    override suspend fun insertResponceData(weather: Responce) {
        return LocalDataSource.insertWeatherHome(weather)
    }

    override suspend fun deletetWeatherData(weather: WeatherData) {
        return LocalDataSource.deleteWeatherData(weather)
    }

    override fun getStoredAlarms(): Flow<List<Alarm>> {
        return LocalDataSource.getStoredAlarms()
    }

    override suspend fun insertAlarms(alarm: Alarm) {
        return LocalDataSource.insertAlarms(alarm)
    }

    override suspend fun deleteAlarms(alarm: Alarm) {
        return LocalDataSource.deleteAlarms(alarm)
    }
}