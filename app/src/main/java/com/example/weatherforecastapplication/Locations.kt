package com.example.weatherforecastapplication

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Locations {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    var longitude:Double = 0.0
    var latitude :Double = 0.0
  lateinit var context: Context
    val LOCATION_PERMISSION_REQUEST_CODE = 10
    lateinit var activirty:MainActivity

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activirty,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getLastLocation()
        }
    }
//    private fun reverseGeocode(location: Location) {
//        try {
//            val addresses: List<Address>? =
//                geocoder.getFromLocation(
//                    location.latitude, location.longitude, 1
//                )
//
//            if (addresses?.isNotEmpty() == true) {
//                val address: Address = addresses[0]
//                val city = address.locality
//                if (!city.isNullOrBlank()) {
//                } else {
//                }
//            } else {
//            }
//        } catch (e: Exception) {
//            Log.e("TAG", "Error during reverse geocoding", e)
//        }
//    }

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