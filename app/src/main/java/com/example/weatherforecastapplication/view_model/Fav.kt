package com.example.weatherforecastapplication.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.model.Repository
import com.example.weatherforecastapplication.model2.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Fav (private val repo: Repository, private val cityName: String) : ViewModel() {
    private var _products: MutableLiveData<WeatherData> = MutableLiveData<WeatherData>()
    val products: LiveData<WeatherData> = _products
    private var _productw: MutableLiveData<List<WeatherData>> = MutableLiveData<List<WeatherData>>()
    val productsw: LiveData<List<WeatherData>> = _productw

    init {
        getAllProducts(cityName, "7f6473d2786753ccda5811e204914fff", )
        getStored()
    }

    private  fun getStored() {
        viewModelScope.launch(Dispatchers.IO) {

            val productList = repo.getStored()
            _productw.postValue(productList)
        }
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