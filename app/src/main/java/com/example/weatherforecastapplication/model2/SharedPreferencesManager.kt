package com.example.weatherforecastapplication.model2

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

        fun getInstance(context: Context) : SharedPreferencesManager {
            return INSTANCE ?: synchronized(context){
                val instance = SharedPreferencesManager(context)
                INSTANCE = instance

                instance
            }

        }
    }
    fun saveAppstate(first:Boolean){
        editor?.putBoolean("AppState",first)
    }
    fun getAppstate():Boolean{
        return sharedPreferences?.getBoolean("AppState", false) !!
    }
    fun saveLatitude(latitude: Float) {
        editor?.putFloat("latitude", latitude)!!.apply()
    }
    fun clearLatitude() {
        editor?.remove("latitude")?.apply()
    }
    fun saveLongitude(longitude: Float) {
        editor?.putFloat("longitude", longitude)!!.apply()
    }
    fun clearLongitude() {
        editor?.remove("longitude")?.apply()
    }
    fun getLatitude(): Float {
        return sharedPreferences?.getFloat("latitude", 0.0f) !!
    }

    fun getLongitude(): Float {
        return sharedPreferences?.getFloat("longitude", 0.0f)!!
    }



    fun saveGpsLat(Gps_latitude:Float){
        editor?.putFloat("Gps_latitude", Gps_latitude)!!.apply()


    }
    fun getGpsLat(): Float{
        return  sharedPreferences?.getFloat("Gps_latitude", 0.0f) !!
    }
    fun saveGpsLon(Gps_longitude:Float){
        editor?.putFloat("Gps_longitude", Gps_longitude)!!.apply()

    }
    fun getGpsLon(): Float{
        return  sharedPreferences?.getFloat("Gps_longitude", 0.0f) !!

    }



    fun saveGpsAddress(Gps_city:String){
        editor?.putString("Gps_city", Gps_city)!!.apply()

    }
    fun getGpsAddress(): String{
        return  sharedPreferences?.getString("Gps_city", "Tanta") !!

    }
    fun saveGpsState(is_checked:Boolean){
        editor?.putBoolean("is_checked", is_checked)!!.apply()

    }

    fun saveMapState(is_checked:Boolean){
        editor?.putBoolean("is_checked", is_checked)!!.apply()

    }
    fun getMapState():Boolean{
        return  sharedPreferences?.getBoolean("is_checked",false)!!
    }
    fun getGpsState():Boolean{
        return  sharedPreferences?.getBoolean("is_checked",false)!!
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
    fun saveLanguageUnit(language: String) {
        editor?.putString("languageCode", language)?.apply()
    }

    fun getLanguageUnit(): String? {
        return sharedPreferences?.getString("languageCode", "en")
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

    fun saveKelvinState(isChecked: Boolean) {
        sharedPreferences?.edit()!!.putBoolean("KelvinState", isChecked).apply()
    }

    fun getKelvinState(): Boolean {
        return sharedPreferences?.getBoolean("KelvinState", false)!!
    }
    fun saveCelsuisState(isChecked: Boolean) {
        sharedPreferences?.edit()!!.putBoolean("CelsuisState", isChecked).apply()
    }

    fun getCelsuisState(): Boolean {
        return sharedPreferences?.getBoolean("CelsuisState", false)!!
    }
    fun saveFahrenheitState(isChecked: Boolean) {
        sharedPreferences?.edit()!!.putBoolean("FahrenheitState", isChecked).apply()
    }

    fun getFahrenheitState(): Boolean {
        return sharedPreferences?.getBoolean("FahrenheitState", false)!!
    }

    fun saveUnits(unit: String) {
        sharedPreferences?.edit()!!.putString("unit", unit).apply()
    }

    fun getUnits(): String {
        return sharedPreferences?.getString("unit", "metric")!!
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