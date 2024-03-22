package com.example.weatherforecastapplication.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapplication.model2.Repository

class FavFactory (private var repo: Repository, var city:String) :
    ViewModelProvider.Factory

{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(Fav::class.java)){
            Fav(repo,city)as T
        }else{
            throw IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}