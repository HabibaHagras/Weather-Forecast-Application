package com.example.weatherforecastapplication.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.model2.Repository
import com.example.weatherforecastapplication.model2.WeatherData
import com.example.weatherforecastapplication.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class Fav (private val repo: Repository, private val cityName: String) : ViewModel() {
    private var _products: MutableLiveData<WeatherData> = MutableLiveData<WeatherData>()
    val products: LiveData<WeatherData> = _products
    private var _productw: MutableLiveData<List<WeatherData>> = MutableLiveData<List<WeatherData>>()
    val productsw: LiveData<List<WeatherData>> = _productw
    private val _weatherStateFlow: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.loading)
    val weatherStateFlow =  _weatherStateFlow.asStateFlow()
    init {
        getStored()
    }

      fun getStored() {
        viewModelScope.launch(Dispatchers.IO) {
//
//            val productList = repo.getStored()
//            _productw.postValue(productList)

            repo.getStored().catch { e->_weatherStateFlow.value=ApiState.fail(e) }
                .collect{it->
                    _weatherStateFlow.value= ApiState.SucessWeatherData(it)
                }
        }
    }

    private fun getAllProducts(
        cityName: String,
        apiKey: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val productList = repo.getWeatherWithCity2(cityName, apiKey).catch { e->_weatherStateFlow.value=ApiState.fail(e) }
                .collect{it->
                    _weatherStateFlow.value= ApiState.SucessedWeather(it)
                }
//            _products.postValue(productList)
        }
    }
    fun insertProducts(product:WeatherData) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("TAG", "insertProducts: ViewMOdel")
            repo.insertWeatherData(product)
            getStored()


        }
    }
    fun deleteWeather(product:WeatherData) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("TAG", "deleteIteamFav: ")
            repo.deletetWeatherData(product)
            getStored()



        }
    }
}

