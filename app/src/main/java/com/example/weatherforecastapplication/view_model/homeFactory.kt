package com.example.weatherforecastapplication.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapplication.model2.Repository
import com.example.weatherforecastapplication.model2.SharedPreferencesManager

class homeFactory (private var repo: Repository, private val sharedPreferenceSource: SharedPreferencesManager) :
    ViewModelProvider.Factory

{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(home::class.java)){
            home(repo,sharedPreferenceSource)as T
        }else{
            throw IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}