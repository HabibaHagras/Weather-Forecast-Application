package com.example.weatherforecastapplication.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.model2.Repository
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
import com.example.weatherforecastapplication.model2.WeatherData
import com.example.weatherforecastapplication.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class notification (private val repo: Repository,
                    private val sharedPreferenceSource: SharedPreferencesManager
): ViewModel() {
    private var _products: MutableLiveData<WeatherData> =
        MutableLiveData<WeatherData>()
    val products: LiveData<WeatherData> = _products
    private val _weatherStateFlow: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.loading)
    val weatherStateFlow =  _weatherStateFlow.asStateFlow()
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var lang: String = "en"
    private var unit: String = ""

    init {

        updateLocationFromSharedPreferences()
//        getAllProducts()
    }

    private fun updateLocationFromSharedPreferences() {
        latitude = sharedPreferenceSource.getGpsLat().toDouble()
        longitude = sharedPreferenceSource.getGpsLon().toDouble()
        lang=sharedPreferenceSource.getLanguageUnit().toString()
        unit=sharedPreferenceSource.getUnits().toString()    }

     fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("TAG", "getAllProducts: ViewMOdel")
            val ProductList=repo.getWeatherWithCity(latitude,longitude,"7f6473d2786753ccda5811e204914fff"
                ,unit,lang).catch { e->_weatherStateFlow.value= ApiState.fail(e) }
                .collect{it->
                    _weatherStateFlow.value= ApiState.SucessedWeather(it)
                }
            Log.i("TAG", "getAllProducts: $ProductList")
//            _products.postValue(ProductList)
        }
    }
}