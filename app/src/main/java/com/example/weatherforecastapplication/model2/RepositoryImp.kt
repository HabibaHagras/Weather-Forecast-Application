package com.example.weatherforecastapplication.model2

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherforecastapplication.dp.WeatherLocalDataSource
import com.example.weatherforecastapplication.network.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlin.collections.List

class RepositoryImp private constructor(
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
    ): Responce {
        Log.i("TAG", "getAllProduct: ProductRepositoryImp")
        return RemoteDataSource.getWeatherOverNetwork(latitude,longitude,apiKey,lang,units)    }

    override suspend fun getWeatherWithCity(latitude: Double,
                                            longitude: Double,
                                            apiKey: String,
                                            units: String): WeatherData {
return RemoteDataSource.getWeatherWithCityOverNetwork(latitude,longitude,apiKey,units)   }

    override suspend fun getWeatherWithCity2(city: String, apiKey: String): WeatherData {
      return  RemoteDataSource.getWeatherWithCity(city,apiKey)
    }

    override suspend fun getStored(): Flow<List<WeatherData>> {
        return LocalDataSource.getStoredProducts()
    }

    override suspend fun insertWeatherData(product: WeatherData) {
        return LocalDataSource.insertWeatherData(product)
    }

    override suspend fun getStoredHome(): Flow<List<Responce>> {
        return LocalDataSource.getStoredWeatherHome()
    }

    override suspend fun insertResponceData(weather: Responce) {
        return LocalDataSource.insertWeatherHome(weather)
    }
}