package com.example.weatherforecastapplication.network
//
//import com.example.weatherforecastapplication.WeatherData
//import com.example.weatherforecastapplication.WeatherForecastResponse
import com.example.weatherforecastapplication.model2.WeatherData
import com.example.weatherforecastapplication.model2.Responce
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Api_Service {
    @GET("weather")
    suspend fun getWeather(
//        @Query("q") city: String,
//        @Query("appid") apiKey: String
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String ,
        @Query("lang") Lang: String

        ): WeatherData
    @GET("forecast")
    suspend fun get5DayForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("lang") Lang: String,
        @Query("units") units: String
    ): Responce

    @GET("weather")
    suspend fun getWeatherWithCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String ,
        @Query("lang") Lang: String,
    ): WeatherData
}


object RetrofitHelper {
    private const val baseURL = "https://api.openweathermap.org/data/2.5/"
    val retrofitInstance = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseURL)
        .build()
}
object API {
    val retrofitService : Api_Service by lazy {
        RetrofitHelper.retrofitInstance.create(Api_Service::class.java)
    }
}