package com.example.weatherforecastapplication.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.model.Repository
import com.example.weatherforecastapplication.model.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Fav (private val repo: Repository, private val cityName: String) : ViewModel() {
    private var _products: MutableLiveData<WeatherData> = MutableLiveData<WeatherData>()
    val products: LiveData<WeatherData> = _products

    init {
        getAllProducts(cityName, "7f6473d2786753ccda5811e204914fff", )
    }

    private fun getAllProducts(
        cityName: String,
        apiKey: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val productList = repo.getWeatherWithCity2(cityName, apiKey)
            _products.postValue(productList)
        }
    }
}