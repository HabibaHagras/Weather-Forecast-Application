package com.example.weatherforecastapplication

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.weatherforecastapplication.network.API.retrofitService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    val LOCATION_PERMISSION_REQUEST_CODE = 10
    private lateinit var textViewCity: TextView
    //private lateinit var textViewTemperature: TextView
    var longitude:Double = 0.0
    var latitude :Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewCity=findViewById(R.id.textViewCity)
        //textViewTemperature=findViewById(R.id.textViewTemperature)

        geocoder = Geocoder(this, Locale.getDefault())

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Log.i("TAG", "onCreate: $latitude+++$longitude ")
        requestLocationPermission()
        GlobalScope.launch(Dispatchers.IO) {
            val weatherForecast = retrofitService.get5DayForecast(latitude, longitude , "7f6473d2786753ccda5811e204914fff")
            val weatherData = retrofitService.getWeather("London,uk", "7f6473d2786753ccda5811e204914fff")
            withContext(Dispatchers.Main) {
                updateUI(weatherForecast)
            }
        }

    }
//    private fun updateUI(weatherData: WeatherData) {
//        findViewById<TextView>(R.id.textViewCity).text = weatherData.name
//        findViewById<TextView>(R.id.textViewTemperature).text =
//            "${weatherData.main.temp.toInt()}°C"
//        val iconUrl = "https://openweathermap.org/img/w/${weatherData.weather[0].icon}.png"
//        Glide.with(this)
//            .load(iconUrl)
//            .into(findViewById(R.id.imageViewWeatherIcon))
//
//    }
private fun updateUI(weatherForecast: WeatherForecastResponse) {
    val currentDate = getCurrentDate()
    val todayEntries = weatherForecast.list.filter { it.dt_txt.startsWith(currentDate) }

    if (todayEntries.isNotEmpty()) {
        val todayWeather = todayEntries[0] // Assuming the first entry represents the current weather for today

       // findViewById<TextView>(R.id.textViewCity).text = "City Name" // Replace with actual city name
        findViewById<TextView>(R.id.textViewTemperature).text =
            "${todayWeather.main.temp.toInt()}°C"
        val iconUrl = "https://openweathermap.org/img/w/${todayWeather.weather[0].icon}.png"
        Glide.with(this)
            .load(iconUrl)
            .into(findViewById(R.id.imageViewWeatherIcon))
    } else {
        Log.i("TAG", "updateUI: elseeeeeee no thig ")    }
}

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getLastLocation()
        }
    }
    private fun reverseGeocode(location: Location) {
        try {
            val addresses: List<Address>? =
                geocoder.getFromLocation(
                    location.latitude, location.longitude, 3
                )

            if (addresses?.isNotEmpty() == true) {
                val address: Address = addresses[2]
                val streetName = address.thoroughfare
               // textViewCity.text = "Latitude: ${address.getAddressLine(0)}"
                textViewCity.text = "Street Name: $streetName"
            } else {
                Log.i("TAG", "reverseGeocode:   ")
            }
        } catch (e: Exception) {
        }
    }
    private fun getLastLocation() {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        updateLocationTextView(it)
                        reverseGeocode(it)
                        Log.i("TAG", "getLastLocation: $latitude+++$longitude ")

                    }
                }
        } catch (se: SecurityException) {
        }
    }
    private fun updateLocationTextView(location: Location) {
         latitude = location.latitude
        longitude = location.longitude
    }

}