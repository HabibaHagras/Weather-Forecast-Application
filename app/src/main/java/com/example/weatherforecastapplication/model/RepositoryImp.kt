package com.example.weatherforecastapplication.model

import android.util.Log
import com.example.weatherforecastapplication.network.RemoteDataSource
import kotlin.collections.List

class RepositoryImp private constructor(
    private var RemoteDataSource: RemoteDataSource
): Repository {

    companion object{
        private var instance: RepositoryImp?=null
        fun getInstance( productRemoteDataSource: RemoteDataSource,
                           )
                : RepositoryImp {
            return instance?: synchronized(this){
                val temp= RepositoryImp(productRemoteDataSource)
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
    ): ForecastEntry {
        Log.i("TAG", "getAllProduct: ProductRepositoryImp")
        return RemoteDataSource.getWeatherOverNetwork(latitude,longitude,apiKey,units)    }

    override suspend fun getWeatherWithCity(city: String, apiKey: String): WeatherData {
return RemoteDataSource.getWeatherWithCityOverNetwork(city,apiKey)   }
}