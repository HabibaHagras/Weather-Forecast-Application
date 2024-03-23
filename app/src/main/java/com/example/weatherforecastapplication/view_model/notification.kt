package com.example.weatherforecastapplication.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.model2.Repository
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
import com.example.weatherforecastapplication.model2.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class notification (private val repo: Repository,
                    private val sharedPreferenceSource: SharedPreferencesManager
): ViewModel() {
    private var _products: MutableLiveData<WeatherData> =
        MutableLiveData<WeatherData>()
    val products: LiveData<WeatherData> = _products
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var lang: String = "en"
    private var unit: String = ""

    init {

        updateLocationFromSharedPreferences()
//        getAllProducts()
    }

    private fun updateLocationFromSharedPreferences() {
        latitude = sharedPreferenceSource.getLatitude().toDouble()
        longitude = sharedPreferenceSource.getLongitude().toDouble()
        lang=sharedPreferenceSource.getLanguageUnit().toString()
        unit=sharedPreferenceSource.getUnits().toString()    }

     fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("TAG", "getAllProducts: ViewMOdel")
            val ProductList=repo.getWeatherWithCity(latitude,longitude,"7f6473d2786753ccda5811e204914fff",unit)
            Log.i("TAG", "getAllProducts: $ProductList")
            _products.postValue(ProductList)
        }
    }
}