package com.example.weatherforecastapplication.network

import android.util.Log
import com.example.weatherforecastapplication.model2.WeatherData
import com.example.weatherforecastapplication.model2.Responce

class RemoteDataSourceImp private constructor():RemoteDataSource{
    private val weather_Service :Api_Service by lazy {
        RetrofitHelper.retrofitInstance.create(Api_Service::class.java)
    }

    companion object{

        private var instance:RemoteDataSourceImp?=null
        fun getInstance():RemoteDataSourceImp{
            return instance?: synchronized(this){
                val temp=RemoteDataSourceImp()
                instance=temp
                temp
            }
        }
    }


    override suspend fun getWeatherOverNetwork(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        lang: String,
        units: String
    ): Responce {
        Log.i("TAG", "getAllProduct: ProductRemoteDataSourceImp")
        val responce= weather_Service.get5DayForecast(latitude,longitude,apiKey,lang,units)
        return responce

    }

    override suspend fun getWeatherWithCityOverNetwork( latitude: Double,
                                                        longitude: Double,
                                                        apiKey: String,
                                                        units: String, lang: String): WeatherData {
        Log.i("TAG", "getAllProduct: ProductRemoteDataSourceImp")
        val responce= weather_Service.getWeather(latitude,longitude,apiKey,units,lang)
        return responce      }

    override suspend fun getWeatherWithCity(city: String, apiKey: String,  units: String,lang: String): WeatherData {
        val responce= weather_Service.getWeatherWithCity(city,apiKey,units,lang)
        return responce
    }
}