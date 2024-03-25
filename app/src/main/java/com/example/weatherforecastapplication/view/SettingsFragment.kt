package com.example.weatherforecastapplication.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.example.weatherforecastapplication.MainActivity2
import com.example.weatherforecastapplication.databinding.FragmentSettingsBinding
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale


class SettingsFragment : Fragment() {
    private var isLanguageChanging = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val LOCATION_PERMISSION_REQUEST_CODE = 10
    private lateinit var geocoder: Geocoder
    lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isNetworkAvailable()) {
            binding.noConnectionIcon.isGone
            binding.gps.setOnCheckedChangeListener { _,isChecked->
                if (isChecked) {
                    SharedPreferencesManager.getInstance(requireContext()).saveGpsState(true)
                    SharedPreferencesManager.getInstance(requireContext()).saveMapState(false)
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
                    requestLocationPermission()
                    geocoder = Geocoder(requireContext(), Locale.getDefault())
                    startActivity(Intent(requireContext(), MainActivity2::class.java))

                }
            }
            binding.map.setOnCheckedChangeListener { _, isChecked ->

                if (isChecked) {
                    SharedPreferencesManager.getInstance(requireContext()).saveGpsState(false)
                    SharedPreferencesManager.getInstance(requireContext()).saveMapState(true)
                    startActivity(Intent(requireContext(), MapsActivity::class.java))
                }
            }
            binding.switchLanguageArabic.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    SharedPreferencesManager.getInstance(requireContext()).saveLanguageUnit("ar")
                    setAppLanguage("ar")
                    startActivity(Intent(requireContext(), MainActivity2::class.java))
                }
            }
            binding.switchLanguageEnglish.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    SharedPreferencesManager.getInstance(requireContext()).saveLanguageUnit("en")
                    setAppLanguage("en")
                    startActivity(Intent(requireContext(), MainActivity2::class.java))
                }
            }
            binding.switchDefaultKelvin.apply {
                isChecked = SharedPreferencesManager.getInstance(requireContext()).getKelvinState()
                setOnCheckedChangeListener { _, isChecked ->
                    binding.switchImperialFahrenheit.isChecked = false
                    binding.switchMetricCelsius.isChecked = false
                    SharedPreferencesManager.getInstance(requireContext()).saveUnits("")
                    SharedPreferencesManager.getInstance(requireContext()).saveCelsuisState(binding.switchMetricCelsius.isChecked)
                    SharedPreferencesManager.getInstance(requireContext()).saveFahrenheitState(binding.switchImperialFahrenheit.isChecked)
                    SharedPreferencesManager.getInstance(requireContext()).saveKelvinState(isChecked)
                    startActivity(Intent(requireContext(), MainActivity2::class.java))
                }
            }
            binding.switchMetricCelsius.apply {
                isChecked = SharedPreferencesManager.getInstance(requireContext()).getCelsuisState()
                setOnCheckedChangeListener { _, isChecked ->
                    binding.switchImperialFahrenheit.isChecked = false
                    binding.switchDefaultKelvin.isChecked = false
                    SharedPreferencesManager.getInstance(requireContext()).saveUnits("metric")
                    SharedPreferencesManager.getInstance(requireContext()).saveCelsuisState(binding.switchDefaultKelvin.isChecked)
                    SharedPreferencesManager.getInstance(requireContext()).saveFahrenheitState(binding.switchImperialFahrenheit.isChecked)
                    SharedPreferencesManager.getInstance(requireContext()).saveCelsuisState(isChecked)
                    startActivity(Intent(requireContext(), MainActivity2::class.java))
                }
            }
            binding.switchImperialFahrenheit.apply {
                isChecked = SharedPreferencesManager.getInstance(requireContext()).getFahrenheitState()
                setOnCheckedChangeListener { _, isChecked ->
                    binding.switchMetricCelsius.isChecked = false
                    binding.switchDefaultKelvin.isChecked = false
                    SharedPreferencesManager.getInstance(requireContext()).saveUnits("imperial")
                    SharedPreferencesManager.getInstance(requireContext()).saveCelsuisState(binding.switchDefaultKelvin.isChecked)
                    SharedPreferencesManager.getInstance(requireContext()).saveFahrenheitState(binding.switchMetricCelsius.isChecked)
                    SharedPreferencesManager.getInstance(requireContext()).saveFahrenheitState(isChecked)
                    startActivity(Intent(requireContext(), MainActivity2::class.java))
                }
            }
            binding.mSec.apply {
                isChecked = SharedPreferencesManager.getInstance(requireContext()).getMperSecState()
                setOnCheckedChangeListener { _, isChecked ->
                    binding.kmHour.isChecked = false
                    SharedPreferencesManager.getInstance(requireContext()).saveWind("m/s")
                    SharedPreferencesManager.getInstance(requireContext()).saveKmperHourState(binding.kmHour.isChecked)
                    SharedPreferencesManager.getInstance(requireContext()).saveMperSecState(isChecked)
                    startActivity(Intent(requireContext(), MainActivity2::class.java)) }
            }
            binding.kmHour.apply {
                isChecked = SharedPreferencesManager.getInstance(requireContext()).getKmperHourState()
                setOnCheckedChangeListener { _, isChecked ->
                    binding.mSec.isChecked = false
                    SharedPreferencesManager.getInstance(requireContext()).saveWind("km/h")
                    SharedPreferencesManager.getInstance(requireContext()).saveMperSecState( binding.mSec.isChecked)
                    SharedPreferencesManager.getInstance(requireContext()).saveKmperHourState(isChecked)
                    startActivity(Intent(requireContext(), MainActivity2::class.java))
                }
            }
    }   else{
        binding.noConnectionIcon.visibility
    }
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
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
                        SharedPreferencesManager.getInstance(requireContext()).saveGpsLat(it.latitude.toFloat())
                        SharedPreferencesManager.getInstance(requireContext()).saveGpsLon(it.longitude.toFloat())
                        reverseGeocode(it)
                    }
                }
        } catch (se: SecurityException) {
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
                    SharedPreferencesManager.getInstance(requireContext()).saveGpsAddress(city)

                } else {
                    println( "City not found")
                }
            } else {
                println( "No address found")
            }
        } catch (e: Exception) {
            Log.e("TAG", "Error during reverse geocoding", e)
            println(  "Error during reverse geocoding")
        }
    }


    private fun setAppLanguage(languageCode: String) {
        val currentLocale = Locale.getDefault()
        val newLocale = Locale(languageCode)
        val sharedPreferencesManager = SharedPreferencesManager.getInstance(requireContext())
        sharedPreferencesManager.saveLanguage(languageCode)
        if (currentLocale != newLocale && !isLanguageChanging) {
            isLanguageChanging = true
            Locale.setDefault(newLocale)
            val configuration = Configuration()
            configuration.locale = newLocale
            val resources: Resources = requireContext().resources
            resources.updateConfiguration(configuration, resources.displayMetrics)
            requireActivity().recreate()
        }
    }



    override fun onResume() {
        super.onResume()
        isLanguageChanging = false
    }

        companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}