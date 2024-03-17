package com.example.weatherforecastapplication.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
import com.example.weatherforecastapplication.model.SharedPreferencesManager

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        lateinit var instance: MapsActivity
    }
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

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
            sharedPreferencesManager.saveLatitude(selectedLatitude.toFloat())
            sharedPreferencesManager.saveLongitude(selectedLongitude.toFloat())

            Toast.makeText(this,"Latitude = $selectedLatitude & Longitude = $selectedLongitude",
                Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity2::class.java))

        }

    }
}