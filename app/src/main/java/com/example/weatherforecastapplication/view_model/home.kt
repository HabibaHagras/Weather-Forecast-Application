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
import com.example.weatherforecastapplication.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class home(private val repo: Repository, private val sharedPreferenceSource: SharedPreferencesManager
): ViewModel() {
//    private var _products: MutableLiveData<Responce> =
//        MutableLiveData<Responce>()
//    val products: LiveData<Responce> = _products
//    private var _Favproducts: MutableLiveData<Responce> =
//        MutableLiveData<Responce>()
//    val Favproducts: LiveData<Responce> = _Favproducts
//
//
//    private var _weatherHome: MutableLiveData<List<Responce>> =
//        MutableLiveData<List<Responce>>()
//    val weatherHome: LiveData<List<Responce>> =  _weatherHome



    private val _weatherStateFlow: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.loading)
    val weatherStateFlow =  _weatherStateFlow.asStateFlow()


    private var location: Locations = Locations()
    private var latitude: Double = 0.0
    private var latitudeGps: Double = 0.0
    private var longitude: Double = 0.0
    private var longitudeGps: Double = 0.0
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
        latitudeGps=sharedPreferenceSource.getGpsLat().toDouble()
        longitudeGps=sharedPreferenceSource.getGpsLon().toDouble()
    }

     fun getAllWeatherMap() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("TAG", "getAllProducts: ViewModel")
            repo.getAllWeather(latitude, longitude,
                "7f6473d2786753ccda5811e204914fff", lang.toString(),unit).catch { e->_weatherStateFlow.value=ApiState.fail(e) }
                .collect{it->
                    _weatherStateFlow.value= ApiState.Sucessed(it)
                }
//            Log.i("TAG", "getAllProducts: $lang")
//
//            _products.postValue(productList)
        }
    }

    fun getAllWeatherFromFav(latitude: Double,
                                  longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("TAG", "getAllProducts: ViewMOdel")
          repo.getAllWeather(latitude,longitude,"7f6473d2786753ccda5811e204914fff",
                lang,unit).catch { e->_weatherStateFlow.value=ApiState.fail(e) }
                .collect{it->
                    _weatherStateFlow.value= ApiState.Sucessed(it)
                }
//            Log.i("TAGMap", "getAllProducts: $latitude   + $longitude")
//            _Favproducts.postValue(ProductList)
        }
    }
    fun getAllWeatherGps(){
        viewModelScope.launch(Dispatchers.IO) {
                repo.getAllWeather(latitudeGps, longitudeGps, "7f6473d2786753ccda5811e204914fff", lang.toString(),unit).catch { e->_weatherStateFlow.value=ApiState.fail(e) }
                    .collect{it->
                        _weatherStateFlow.value= ApiState.Sucessed(it)
                    }
//            _products.postValue(productList)
        }
    }

    fun getStoredHome(){
        viewModelScope.launch(Dispatchers.IO) {
//            val ProductList = repo.getStoredHome()
//            _weatherHome.postValue(ProductList)
           repo.getStoredHome().catch { e->_weatherStateFlow.value=ApiState.fail(e) }
               .collect{it->
                   _weatherStateFlow.value= ApiState.Sucess(it)
               }

        }

    }
    fun insertHome(responce: Responce){
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertResponceData(responce)
            getStoredHome()

        }

    }


}
