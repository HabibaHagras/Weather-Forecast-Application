package com.example.weatherforecastapplication.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.model2.Alarm
import com.example.weatherforecastapplication.model2.Repository
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
import com.example.weatherforecastapplication.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Calendar

class AlarmSound (private val repo: Repository, private val sharedPreferenceSource: SharedPreferencesManager) : ViewModel() {
    private val _weatherStateFlow: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.loading)
    val weatherStateFlow =  _weatherStateFlow.asStateFlow()
    private var latitude: Double = 0.0
    private var latitudeGps: Double = 0.0
    private var longitude: Double = 0.0
    private var longitudeGps: Double = 0.0
    private var lang: String = "en"
    private var unit: String = ""

    init {
        updateLocationFromSharedPreferences()
        getStored()
    }
    private fun updateLocationFromSharedPreferences() {
        latitude = sharedPreferenceSource.getLatitude().toDouble()
        longitude = sharedPreferenceSource.getLongitude().toDouble()
        lang=sharedPreferenceSource.getLanguageUnit().toString()
        unit=sharedPreferenceSource.getUnits().toString()
        latitudeGps=sharedPreferenceSource.getGpsLat().toDouble()
        longitudeGps=sharedPreferenceSource.getGpsLon().toDouble()
    }


    fun insertAlarm(lat:Double,log:Double,year:Int,mounth:Int,day:Int,hour: Int, minute: Int,req:Int) {
        val alarm = Alarm(lat, log, hour, minute, mounth, year, day, req)
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertAlarms(alarm)
        }

        getStored()
    }

        fun getStored() {
            viewModelScope.launch(Dispatchers.IO) {
                repo.getStoredAlarms().catch { e->_weatherStateFlow.value=ApiState.fail(e) }
                    .collect{it->
                        _weatherStateFlow.value= ApiState.SucessedAlarm(it)
                    }
            }
        }


    fun delete(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAlarms(alarm)
        }
    }
    }
