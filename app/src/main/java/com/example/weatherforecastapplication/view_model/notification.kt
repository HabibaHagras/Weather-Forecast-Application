package com.example.weatherforecastapplication.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.model.ForecastEntry
import com.example.weatherforecastapplication.model.Repository
import com.example.weatherforecastapplication.model.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class notification (private val repo: Repository): ViewModel() {
    private var _products: MutableLiveData<WeatherData> =
        MutableLiveData<WeatherData>()
    val products: LiveData<WeatherData> = _products

    init {
        getAllProducts("Cairo,eg","7f6473d2786753ccda5811e204914fff")
    }

    private fun getAllProducts(     city: String,
                                    apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("TAG", "getAllProducts: ViewMOdel")
            val ProductList=repo.getWeatherWithCity(city,apiKey)
            Log.i("TAG", "getAllProducts: $ProductList")

            _products.postValue(ProductList)
        }
    }
}