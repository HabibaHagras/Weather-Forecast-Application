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
    fun saveLanguage(languageCode: String) {
        editor?.putString("language", languageCode)?.apply()
    }

    fun getLanguage(): String? {
        return sharedPreferences?.getString("language", null)
    }
    fun saveMetricState(isChecked: Boolean) {
        sharedPreferences?.edit()!!.putBoolean("metricState", isChecked).apply()
    }

    fun getMetricState(): Boolean {
        return sharedPreferences?.getBoolean("metricState", false)!!
    }
    fun saveStandardState(isChecked: Boolean) {
        sharedPreferences?.edit()!!.putBoolean("standardState", isChecked).apply()
    }

    fun getStandardState(): Boolean {
        return sharedPreferences?.getBoolean("standardState", false)!!
    }
    fun clearPreferences() {
        editor?.clear()!!.apply()
    }

}