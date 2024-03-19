package com.example.weatherforecastapplication.model

import android.content.Context
import android.content.SharedPreferences

class  SharedPreferencesManager private constructor(context: Context){
   // private val sharedPreferences = context.getSharedPreferences("LocationPrefs", Context.MODE_PRIVATE)
    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    init {
        sharedPreferences = context.getSharedPreferences("LocationPrefs", Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()
    }

    companion object {
        @Volatile
        private var INSTANCE : SharedPreferencesManager? = null

        fun getInstance(context: Context) : SharedPreferencesManager{
            return INSTANCE ?: synchronized(context){
                val instance = SharedPreferencesManager(context)
                INSTANCE = instance

                instance
            }

        }
    }
    fun saveLatitude(latitude: Float) {
        editor?.putFloat("latitude", latitude)!!.apply()
    }

    fun saveLongitude(longitude: Float) {
        editor?.putFloat("longitude", longitude)!!.apply()
    }

    fun getLatitude(): Float {
        return sharedPreferences?.getFloat("latitude", 0.0f) !!
    }

    fun getLongitude(): Float {
        return sharedPreferences?.getFloat("longitude", 0.0f)!!
    }
    fun saveFavLatitude(latitude: Float) {
        editor?.putFloat("fav_latitude", latitude)!!.apply()
    }

    fun saveFavLongitude(longitude: Float) {
        editor?.putFloat("fav_longitude", longitude)!!.apply()
    }

    fun getFavLatitude(): Float {
        return sharedPreferences?.getFloat("fav_latitude", 0.0f) !!
    }

    fun getFavLongitude(): Float {
        return sharedPreferences?.getFloat("fav_longitude", 0.0f)!!
    }



    fun saveLanguage(languageCode: String) {
        editor?.putString("language", languageCode)?.apply()
    }

    fun getLanguage(): String? {
        return sharedPreferences?.getString("language", null)
    }


    fun saveFavCity(city: String) {
        editor?.putString("fav_city", city)!!.apply()
    }

    fun getFavCity(): String {
        return sharedPreferences?.getString("fav_city", "") !!
    }

    fun saveMperSecState(isChecked: Boolean) {
        sharedPreferences?.edit()!!.putBoolean("MperSec", isChecked).apply()
    }

    fun getMperSecState(): Boolean {
        return sharedPreferences?.getBoolean("MperSec", false)!!
    }



    fun saveWind(wind: String) {
        sharedPreferences?.edit()!!.putString("Wind", wind).apply()
    }

    fun getUnitWind(): String {
        return sharedPreferences?.getString("Wind", "m/s")!!
    }

    fun saveKmperHourState(isChecked: Boolean) {
        sharedPreferences?.edit()!!.putBoolean("KmperHourState", isChecked).apply()
    }

    fun getKmperHourState(): Boolean {
        return sharedPreferences?.getBoolean("KmperHourState", false)!!
    }
    fun clearPreferences() {
        editor?.clear()!!.apply()
    }

}