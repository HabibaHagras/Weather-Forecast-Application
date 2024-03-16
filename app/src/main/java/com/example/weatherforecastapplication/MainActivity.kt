package com.example.weatherforecastapplication

import WeatherLocalDataSourceImp
import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecastapplication.model.RepositoryImp
import com.example.weatherforecastapplication.network.RemoteDataSourceImp
import com.example.weatherforecastapplication.model2.Responce
import com.example.weatherforecastapplication.view.HomeAdapter
import com.example.weatherforecastapplication.view.NotificationFragment
import com.example.weatherforecastapplication.view_model.home
import com.example.weatherforecastapplication.view_model.homeFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.collections.List


class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    lateinit var allProductFactroy:homeFactory
    lateinit var allProductViewModel: home
    val LOCATION_PERMISSION_REQUEST_CODE = 10
    lateinit var mAdapter: HomeAdapter
    lateinit var rv: RecyclerView
    lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var textViewCity: TextView
    //private lateinit var textViewTemperature: TextView
    var longitude:Double = 0.0
    var latitude :Double = 0.0
    lateinit var o:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewCity=findViewById(R.id.textViewCity)
        rv = findViewById(R.id.rv)
        o=findViewById(R.id.button)
        o.setOnClickListener {  val fragment = NotificationFragment()

            // Use supportFragmentManager directly without requireActivity()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null) // Optional: add to back stack if you want to enable back navigation
                .commit()}
        mLayoutManager = LinearLayoutManager(this,  RecyclerView.HORIZONTAL, false)
        mAdapter = HomeAdapter(this)
//        val weatherData: List<Listt> = // populate this list with your weather data
//        val adapter = HomeAdapter(weatherData)
        rv.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager}
        //textViewTemperature=findViewById(R.id.textViewTemperature)

        geocoder = Geocoder(this, Locale.getDefault())

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Log.i("TAG", "onCreate: $latitude+++$longitude ")
        requestLocationPermission()

        allProductFactroy= homeFactory(
            RepositoryImp.getInstance(
            RemoteDataSourceImp.getInstance(),WeatherLocalDataSourceImp(this) ))
        allProductViewModel= ViewModelProvider(this,allProductFactroy).get(home::class.java)

        allProductViewModel.products.observe(this,
            object: Observer<Responce> {
                override fun onChanged(value: Responce) {

                   mAdapter.setData(value.list)
                    Log.i("TAG", "oooooooooooooooo :$value ")

                    updateUI(value)
                }


            })


//
//        GlobalScope.launch(Dispatchers.IO) {
//            val weatherForecast = retrofitService.get5DayForecast(latitude, longitude , "7f6473d2786753ccda5811e204914fff")
//            Log.i("TAG", "API Response: $weatherForecast")
//
//            // val weatherData = retrofitService.getWeather("London,uk", "7f6473d2786753ccda5811e204914fff")
//            withContext(Dispatchers.Main) {
//                updateUI(weatherForecast)
//            }
//        }

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
private fun updateUI(weatherForecast: Responce) {
    val currentDate = getCurrentDate()
    Log.i("TAG", "currentDate: $currentDate")

    val todayEntries = weatherForecast.list
//        .filter { it.dt_txt?.startsWith(currentDate) == true }
    Log.i("TAG", "todayEntries: $todayEntries")
    if (todayEntries.isNotEmpty()) {
        val todayWeather = todayEntries[0]
        Log.i("TAG", "todayWeather: $todayEntries")

        findViewById<TextView>(R.id.textViewTemperature).text =
            "${todayWeather.main.temp.toInt()}°C"
        Log.i("TAG", "weeeeether: ${todayWeather.weather[0].description}")

        val iconCode = todayWeather.weather[0].icon
        val iconUrl = getIconUrl(iconCode)

        Glide.with(this)
            .load(iconUrl)
            .into(findViewById(R.id.imageViewWeatherIsskcon))
    } else {
        Log.i("TAG", "updateUI: elseeeeeee no thing")
    }
}


    private fun getIconUrl(iconCode: String): String {
        // You may need to adjust the base URL or handle different icon codes here
        return "https://openweathermap.org/img/w/$iconCode.png"
    }

//    private fun updateUI(weatherForecast: WeatherForecastResponse) {
//    val currentDate = getCurrentDate()
////    val todayEntries = weatherForecast.list.filter { it.dt_txt.startsWith(currentDate) }
//    val todayEntries = weatherForecast.list
//    if (todayEntries.isNotEmpty()) {
//        val todayWeather = todayEntries[0] // Assuming the first entry represents the current weather for today
//
//       // findViewById<TextView>(R.id.textViewCity).text = "City Name" // Replace with actual city name
//        findViewById<TextView>(R.id.textViewTemperature).text =
//            "${todayWeather.main.temp.toInt()}°C"
//        val iconUrl = "https://openweathermap.org/img/w/${todayWeather.weather[0].icon}.png"
//        Glide.with(this)
//            .load(iconUrl)
//            .into(findViewById(R.id.imageViewWeatherIcon))
//    } else {
//        Log.i("TAG", "updateUI: elseeeeeee no thig ")    }
//}

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
                    location.latitude, location.longitude, 1
                )

            if (addresses?.isNotEmpty() == true) {
                val address: Address = addresses[0]
                val city = address.locality
                if (!city.isNullOrBlank()) {
                    textViewCity.text = "City: $city"
                } else {
                    textViewCity.text = "City not found"
                }
            } else {
                textViewCity.text = "No address found"
            }
        } catch (e: Exception) {
            Log.e("TAG", "Error during reverse geocoding", e)
            textViewCity.text = "Error during reverse geocoding"
        }
    }

    //    private fun reverseGeocode(location: Location) {
//        try {
//            val addresses: List<Address>? =
//                geocoder.getFromLocation(
//                    location.latitude, location.longitude, 3
//                )
//
//            if (addresses?.isNotEmpty() == true) {
//                val address: Address = addresses[2]
//                val streetName = address.thoroughfare
//               // textViewCity.text = "Latitude: ${address.getAddressLine(0)}"
//                textViewCity.text = "Street Name: $streetName"
//            } else {
//                Log.i("TAG", "reverseGeocode:   ")
//            }
//        } catch (e: Exception) {
//        }
//    }
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
     fun updateLocationTextView(location: Location) {
         latitude = location.latitude
        longitude = location.longitude

    }

}