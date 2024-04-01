package com.example.weatherforecastapplication.view

import WeatherLocalDataSourceImp
import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapplication.MainActivity2
import com.example.weatherforecastapplication.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.example.weatherforecastapplication.databinding.ActivityMapsBinding
import com.example.weatherforecastapplication.model2.Main
import com.example.weatherforecastapplication.model2.RepositoryImp
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
import com.example.weatherforecastapplication.model2.Clouds
import com.example.weatherforecastapplication.model2.Coord
import com.example.weatherforecastapplication.model2.CoordWeather
import com.example.weatherforecastapplication.model2.Responce
import com.example.weatherforecastapplication.model2.Sys
import com.example.weatherforecastapplication.model2.Weather
import com.example.weatherforecastapplication.model2.WeatherData
import com.example.weatherforecastapplication.model2.Wind
import com.example.weatherforecastapplication.network.RemoteDataSourceImp
import com.example.weatherforecastapplication.view_model.Fav
import com.example.weatherforecastapplication.view_model.FavFactory
import com.example.weatherforecastapplication.view_model.Maps
import com.example.weatherforecastapplication.view_model.MapsFactory
import com.example.weatherforecastapplication.view_model.home
import com.example.weatherforecastapplication.view_model.homeFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        lateinit var instance: MapsActivity
    }
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    lateinit var allFavViewModel: Fav
    lateinit var allFavFactroy: FavFactory
    lateinit var allMapsFactroy: MapsFactory
    lateinit var allMapsViewModel: Maps
     var city_name:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        instance=this
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener { latLng ->
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7f))
            val selectedLatitude = latLng.latitude
            val selectedLongitude = latLng.longitude
            val receivedIntent = intent
            if (receivedIntent != null && receivedIntent.hasExtra("favorite")) {
                sharedPreferencesManager.saveFavLatitude(selectedLatitude.toFloat())
                sharedPreferencesManager.saveFavLongitude(selectedLongitude.toFloat())
                Log.i("TAGMap", "onMapReady:$selectedLatitude + $selectedLongitude ")
                allMapsFactroy = MapsFactory(
                    RepositoryImp.getInstance(
                        RemoteDataSourceImp.getInstance(),
                        WeatherLocalDataSourceImp(this)
                    ), SharedPreferencesManager.getInstance(this)
                )
                allMapsViewModel =
                    ViewModelProvider(this, allMapsFactroy).get(Maps::class.java)
                val geocoder = Geocoder(this)
                val addresses = geocoder.getFromLocation(selectedLatitude, selectedLongitude, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    val cityName = addresses[0].getAddressLine(0)
                    val lat = addresses[0].latitude
                    val lon = addresses[0].longitude
                    if (cityName != null) {
                        sharedPreferencesManager.saveFavCity(cityName.toString())
                        sharedPreferencesManager.saveFavCity(cityName.toString())
                        allFavFactroy = FavFactory(
                            RepositoryImp.getInstance(
                                RemoteDataSourceImp.getInstance(),
                                WeatherLocalDataSourceImp(this)
                            ), city_name,SharedPreferencesManager.getInstance(this)
                        )
                        allFavViewModel =
                            ViewModelProvider(this, allFavFactroy).get(Fav::class.java)
                        allFavViewModel.insertProducts(
                            WeatherData(
                                id = 0,
                                base = "baseValue",
                                clouds = Clouds(all = 0),
                                cod = 200,
                                coord = CoordWeather(selectedLongitude.toDouble() ,selectedLatitude.toDouble()),
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
                                name = cityName.toString(),
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
                }

               }
                else {
                    Toast.makeText(
                        this, "this is not a city",
                        Toast.LENGTH_LONG     ).show()              }
            }
        else {
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