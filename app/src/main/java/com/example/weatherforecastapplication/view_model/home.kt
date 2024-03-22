package com.example.weatherforecastapplication.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.Locations
import com.example.weatherforecastapplication.model2.Repository
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
import com.example.weatherforecastapplication.model2.Responce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class home(private val repo: Repository, private val sharedPreferenceSource: SharedPreferencesManager
): ViewModel() {
    private var _products: MutableLiveData<Responce> =
        MutableLiveData<Responce>()
    val products: LiveData<Responce> = _products
    private var _Favproducts: MutableLiveData<Responce> =
        MutableLiveData<Responce>()
    val Favproducts: LiveData<Responce> = _Favproducts
    private var location: Locations = Locations()
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var lang: String = "en"
    private var unit: String = ""

    init {
        updateLocationFromSharedPreferences()
//        getAllProducts()
//        getAllWeatherFromFav()
    }

    private fun updateLocationFromSharedPreferences() {
//        val sharedPreferences = MapsActivity.instance.getSharedPreferences("LocationPrefs", Context.MODE_PRIVATE)
//        latitude = sharedPreferences.getFloat("latitude", 0.0f).toDouble()
//        longitude = sharedPreferences.getFloat("longitude", 0.0f).toDouble()//
        latitude = sharedPreferenceSource.getLatitude().toDouble()
        longitude = sharedPreferenceSource.getLongitude().toDouble()
        lang=sharedPreferenceSource.getLanguageUnit().toString()
        unit=sharedPreferenceSource.getUnits().toString()
    }

     fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("TAG", "getAllProducts: ViewModel")
            val productList = repo.getAllWeather(latitude, longitude, "7f6473d2786753ccda5811e204914fff", lang.toString(),unit)
            Log.i("TAG", "getAllProducts: $lang")

            _products.postValue(productList)
        }
    }

    fun getAllWeatherFromFav(latitude: Double,
                                  longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("TAG", "getAllProducts: ViewMOdel")
            val ProductList=repo.getAllWeather(latitude,longitude,"7f6473d2786753ccda5811e204914fff",lang,unit)
            Log.i("TAGMap", "getAllProducts: $latitude   + $longitude")
            _Favproducts.postValue(ProductList)
        }
    }
}
//    init {
////        loction=Locations()
////        l=MainActivity()
//        val sharedPreferences = context.getSharedPreferences("LocationPrefs", Context.MODE_PRIVATE)
//        val latitude = sharedPreferences.getFloat("latitude", 0.0f).toDouble()
//        val longitude = sharedPreferences.getFloat("longitude", 0.0f).toDouble()
//       // getAllProducts(30.7914776,30.9957296,"7f6473d2786753ccda5811e204914fff","metric")
//       getAllProducts(latitude,longitude,"7f6473d2786753ccda5811e204914fff","metric")
//    }
//
//     fun getAllWeatherFromFav(     latitude: Double,
//                                    longitude: Double,
//                                    apiKey: String,
//                                    units: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            Log.i("TAG", "getAllProducts: ViewMOdel")
//            val ProductList=repo.getAllWeather(latitude,longitude,apiKey,units)
//            Log.i("TAG", "getAllProducts: $ProductList")
//
//            _products.postValue(ProductList)
//        }
//  }
//}