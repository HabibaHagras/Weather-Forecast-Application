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

class Fav (private val repo: Repository, private val cityName: String ,private val sharedPreferenceSource: SharedPreferencesManager) : ViewModel() {
//    private var _products: MutableLiveData<WeatherData> = MutableLiveData<WeatherData>()
//    val products: LiveData<WeatherData> = _products
//    private var _productw: MutableLiveData<List<WeatherData>> = MutableLiveData<List<WeatherData>>()
//    val productsw: LiveData<List<WeatherData>> = _productw
    private val _weatherStateFlow: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.loading)
    val weatherStateFlow =  _weatherStateFlow.asStateFlow()
    private var latitude: Double = 0.0
    private var latitudeGps: Double = 0.0
    private var longitude: Double = 0.0
    private var longitudeGps: Double = 0.0
    private var lang: String = "en"
    private var unit: String = ""
    init {
        getStored()
    }
    private fun updateLocationFromSharedPreferences() {
//        val sharedPreferences = MapsActivity.instance.getSharedPreferences("LocationPrefs", Context.MODE_PRIVATE)
//        latitude = sharedPreferences.getFloat("latitude", 0.0f).toDouble()
//        longitude = sharedPreferences.getFloat("longitude", 0.0f).toDouble()//
        latitude = sharedPreferenceSource.getLatitude().toDouble()
        longitude = sharedPreferenceSource.getLongitude().toDouble()
        lang=sharedPreferenceSource.getLanguageUnit().toString()
        unit=sharedPreferenceSource.getUnits().toString()
        latitudeGps=sharedPreferenceSource.getGpsLat().toDouble()
        longitudeGps=sharedPreferenceSource.getGpsLon().toDouble()
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

     fun getAllWeather(cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getWeatherWithCity2(cityName, "7f6473d2786753ccda5811e204914fff",unit,lang).catch { e->_weatherStateFlow.value=ApiState.fail(e) }
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

