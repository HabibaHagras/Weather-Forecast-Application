package com.example.weatherforecastapplication.model

import android.util.Log
import com.example.weatherforecastapplication.dp.WeatherLocalDataSource
import com.example.weatherforecastapplication.network.RemoteDataSource
import com.example.weatherforecastapplication.model2.Responce
import com.example.weatherforecastapplication.model2.WeatherData
import kotlin.collections.List

class RepositoryImp private constructor(
    private var RemoteDataSource: RemoteDataSource,private var LocalDataSource:WeatherLocalDataSource
): Repository {

    companion object{
        private var instance: RepositoryImp?=null
        fun getInstance( productRemoteDataSource: RemoteDataSource,
                         productLocalDataSource: WeatherLocalDataSource,        )
                : RepositoryImp {
            return instance?: synchronized(this){
                val temp= RepositoryImp(productRemoteDataSource,productLocalDataSource)
                instance=temp
                temp
            }
        }
    }

    override suspend fun getAllWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        units: String
    ): Responce {
        Log.i("TAG", "getAllProduct: ProductRepositoryImp")
        return RemoteDataSource.getWeatherOverNetwork(latitude,longitude,apiKey,units)    }

    override suspend fun getWeatherWithCity(latitude: Double,
                                            longitude: Double,
                                            apiKey: String,
                                            units: String): WeatherData {
return RemoteDataSource.getWeatherWithCityOverNetwork(latitude,longitude,apiKey,units)   }

    override suspend fun getWeatherWithCity2(city: String, apiKey: String): WeatherData {
      return  RemoteDataSource.getWeatherWithCity(city,apiKey)
    }

    override suspend fun getStored(): List<WeatherData> {
        return LocalDataSource.getStoredProducts()
    }

    override suspend fun insertWeatherData(product: WeatherData) {
        return LocalDataSource.insertWeatherData(product)
    }
}