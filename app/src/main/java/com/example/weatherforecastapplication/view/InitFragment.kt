package com.example.weatherforecastapplication.view

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weatherforecastapplication.MainActivity2
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class InitFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val LOCATION_PERMISSION_REQUEST_CODE = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_init, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        requestLocationPermission()
        if (!SharedPreferencesManager.getInstance(requireContext()).getAppstate()){
            SharedPreferencesManager.getInstance(requireContext()).saveAppstate(true)
            showLocationSelectionDialog()

        }else{
            startActivity(Intent(requireContext(), MainActivity2::class.java))
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InitFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
    fun showLocationSelectionDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.init_dialoge, null)
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
        dialogView.findViewById<Button>(R.id.Gps)?.setOnClickListener {
            alertDialog.dismiss()
            requestLocationPermission()
            startActivity(Intent(requireContext(), MainActivity2::class.java))

        }
        dialogView.findViewById<Button>(R.id.Map)?.setOnClickListener {
            alertDialog.dismiss()
            startActivity(Intent(requireContext(), MapsActivity::class.java))
        }
    }
    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getLastLocation()
        }
    }
    private fun getLastLocation() {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        updateLocationTextView(it)
                    }
                }
        } catch (se: SecurityException) {
        }
    }
    private fun updateLocationTextView(location: Location) {
        SharedPreferencesManager.getInstance(requireContext()).saveGpsLat(location.latitude.toFloat())
        SharedPreferencesManager.getInstance(requireContext()).saveGpsLon(location.longitude.toFloat())
    }
}