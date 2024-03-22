package com.example.weatherforecastapplication.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.model2.Repository
import com.example.weatherforecastapplication.model2.Responce
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Maps (private val repo: Repository, private val sharedPreferenceSource: SharedPreferencesManager
): ViewModel() {
    private var _weatherMaps: MutableLiveData<Responce> =
        MutableLiveData<Responce>()
    val weatherMaps: LiveData<Responce> = _weatherMaps
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var lang: String = "en"
    private var unit: String = ""
    init {
        updateLocationFromSharedPreferences()
        getWeatherCity()
    }

    private fun updateLocationFromSharedPreferences() {
        latitude = sharedPreferenceSource.getFavLatitude().toDouble()
        longitude = sharedPreferenceSource.getFavLongitude().toDouble()
        lang=sharedPreferenceSource.getLanguageUnit().toString()
        unit=sharedPreferenceSource.getUnits().toString()
    }

    private fun getWeatherCity() {
        viewModelScope.launch(Dispatchers.IO) {
            val productList = repo.getAllWeather(latitude, longitude, "7f6473d2786753ccda5811e204914fff", lang.toString(),unit)
            _weatherMaps.postValue(productList)
        }
    }
}