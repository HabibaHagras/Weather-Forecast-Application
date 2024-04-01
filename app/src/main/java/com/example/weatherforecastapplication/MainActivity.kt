package com.example.weatherforecastapplication

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.view.home_view.HomeAdapter
import com.example.weatherforecastapplication.view.home_view.HomeFragment
import com.example.weatherforecastapplication.view_model.home
import com.example.weatherforecastapplication.view_model.homeFactory
import com.google.android.gms.location.FusedLocationProviderClient


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
    companion object {
        lateinit var instance: MainActivity
    }
    override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            // Replace the current fragment with HomeFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment())
                .commit()
        }
}
