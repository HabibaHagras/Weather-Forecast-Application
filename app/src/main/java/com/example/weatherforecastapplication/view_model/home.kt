package com.example.weatherforecastapplication.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.Locations
import com.example.weatherforecastapplication.MainActivity
import com.example.weatherforecastapplication.model.Repository
import com.example.weatherforecastapplication.model2.Responce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class home(private val repo: Repository): ViewModel() {
    private var _products: MutableLiveData<Responce> =
        MutableLiveData<Responce>()
    val products: LiveData<Responce> = _products
     var loction:Locations
     var l :MainActivity
    init {
        loction=Locations()
        l=MainActivity()
        getAllProducts(30.7914776,30.9957296,"7f6473d2786753ccda5811e204914fff","metric")
      // getAllProducts(l.latitude,l.longitude,"7f6473d2786753ccda5811e204914fff","metric")
    }

    private fun getAllProducts(     latitude: Double,
                                    longitude: Double,
                                    apiKey: String,
                                    units: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("TAG", "getAllProducts: ViewMOdel")
            val ProductList=repo.getAllWeather(latitude,longitude,apiKey,units)
            Log.i("TAG", "getAllProducts: $ProductList")

            _products.postValue(ProductList)
        }
    }
}