package com.example.weatherforecastapplication.view

import WeatherLocalDataSourceImp
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapplication.HomeFragment
import com.example.weatherforecastapplication.MainActivity
import com.example.weatherforecastapplication.MainActivity2
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.SplashActivity

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.weatherforecastapplication.databinding.ActivityMapsBinding
import com.example.weatherforecastapplication.model2.Main
import com.example.weatherforecastapplication.model.RepositoryImp
import com.example.weatherforecastapplication.model.SharedPreferencesManager
import com.example.weatherforecastapplication.model2.Clouds
import com.example.weatherforecastapplication.model2.Coord
import com.example.weatherforecastapplication.model2.Sys
import com.example.weatherforecastapplication.model2.Weather
import com.example.weatherforecastapplication.model2.WeatherData
import com.example.weatherforecastapplication.model2.Wind
import com.example.weatherforecastapplication.network.RemoteDataSourceImp
import com.example.weatherforecastapplication.view_model.Fav
import com.example.weatherforecastapplication.view_model.FavFactory
import com.google.android.material.navigation.NavigationView

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        lateinit var instance: MapsActivity
    }
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    lateinit var allFavViewModel: Fav
    lateinit var allFavFactroy: FavFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        instance=this
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener { latLng ->
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7f))
            val selectedLatitude = latLng.latitude
            val selectedLongitude = latLng.longitude
//            sharedPreferencesManager.saveLatitude(selectedLatitude.toFloat())
//            sharedPreferencesManager.saveLongitude(selectedLongitude.toFloat())
//            Toast.makeText(
//                this, "Latitude = $selectedLatitude & Longitude = $selectedLongitude",
//                Toast.LENGTH_LONG
//            ).show()
            val receivedIntent = intent
            if (receivedIntent != null && receivedIntent.hasExtra("favorite")) {
                sharedPreferencesManager.saveFavLatitude(selectedLatitude.toFloat())
                sharedPreferencesManager.saveFavLongitude(selectedLongitude.toFloat())
                val geocoder = Geocoder(this)
                val addresses = geocoder.getFromLocation(selectedLatitude, selectedLongitude, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    val cityName = addresses[0].locality
                    if (cityName != null) {
                        sharedPreferencesManager.saveFavCity(cityName)
                        // Your further logic here

                        sharedPreferencesManager.saveFavCity(cityName)
                        allFavFactroy = FavFactory(
                            RepositoryImp.getInstance(
                                RemoteDataSourceImp.getInstance(),
                                WeatherLocalDataSourceImp(this)
                            ), cityName
                        )
                        allFavViewModel =
                            ViewModelProvider(this, allFavFactroy).get(Fav::class.java)
                        allFavViewModel.insertProducts(
                            WeatherData(
                                base = "baseValue",
                                clouds = Clouds(all = 0),
                                cod = 200,
                                coord = Coord(lat = 0.0, lon = 0.0),
                                dt = 123456789,
                                main = Main(
                                    feels_like = 0.0,
                                    grnd_level = 0,
                                    humidity = 0,
                                    pressure = 0,
                                    sea_level = 0,
                                    temp = 0.0,
                                    temp_kf = 0.0,
                                    temp_max = 0.0,
                                    temp_min = 0.0
                                ),
                                name = cityName,
                                sys = Sys(""),
                                timezone = 0,
                                visibility = 1000,
                                weather = listOf(
                                    Weather(
                                        description = "description",
                                        icon = "icon",
                                        id = 800,
                                        main = "main"
                                    )
                                ),
                                wind = Wind(deg = 0, gust = 0.0, speed = 0.0)
                            )
                        )
                        finish()
                    }           else {
                    Toast.makeText(this, "City name is null", Toast.LENGTH_LONG).show()


                }}else {
                    Toast.makeText(
                        this, "this is not a city",
                        Toast.LENGTH_LONG     ).show()              }
            } else {
                sharedPreferencesManager.saveLatitude(selectedLatitude.toFloat())
                sharedPreferencesManager.saveLongitude(selectedLongitude.toFloat())
                Toast.makeText(
                    this, "Latitude = $selectedLatitude & Longitude = $selectedLongitude",
                    Toast.LENGTH_LONG
                ).show()
                startActivity(Intent(this, MainActivity2::class.java))
            }

        }
    }
}