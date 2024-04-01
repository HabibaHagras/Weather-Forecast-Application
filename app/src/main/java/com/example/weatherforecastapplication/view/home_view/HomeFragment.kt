package com.example.weatherforecastapplication.view.home_view

import WeatherLocalDataSourceImp
import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.model2.RepositoryImp
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
import com.example.weatherforecastapplication.network.RemoteDataSourceImp
import com.example.weatherforecastapplication.model2.Responce
import com.example.weatherforecastapplication.network.ApiState
import com.example.weatherforecastapplication.view.MapsActivity
import com.example.weatherforecastapplication.view.NetworkAvailability
import com.example.weatherforecastapplication.view_model.home
import com.example.weatherforecastapplication.view_model.homeFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    lateinit var allProductFactroy: homeFactory
    lateinit var allProductViewModel: home
    lateinit var FeelsLike:TextView
    val LOCATION_PERMISSION_REQUEST_CODE = 10
    lateinit var mAdapter: HomeAdapter
    lateinit var mWeekAdapter: HomeWeekAdapter
    lateinit var mWeekLayoutManager: LinearLayoutManager
    lateinit var rv: RecyclerView
    lateinit var rvWeek: RecyclerView
    lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var textViewCity: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var progressBar1: ProgressBar
    private lateinit var progressBar2: ProgressBar
    var longitude: Double = 0.0
    var latitude: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FeelsLike=view.findViewById(R.id.Feels_Like)
        textViewCity = view.findViewById(R.id.textViewCity)
        rv = view.findViewById(R.id.rv)
        rvWeek = view.findViewById(R.id.rv_Week)
        val favName = arguments?.getString("selected_city")
        val favLat = arguments?.getDouble("selected_lat")
        val favLon = arguments?.getDouble("selected_lon")
        progressBar = view.findViewById(R.id.progressBar)
        progressBar1 = view.findViewById(R.id.progressBar3)
        progressBar2 = view.findViewById(R.id.progressBar2)

        Log.i("TAGMap", "OnCLickIteamFav:$favLat + $favLon  ")
        mWeekLayoutManager= LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        mLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        mAdapter = HomeAdapter(requireContext())
        mWeekAdapter = HomeWeekAdapter(requireContext())
        allProductFactroy = homeFactory(
            RepositoryImp.getInstance(
                RemoteDataSourceImp.getInstance(),WeatherLocalDataSourceImp(requireContext())
            ), SharedPreferencesManager.getInstance(requireContext()
            ))
        allProductViewModel = ViewModelProvider(this, allProductFactroy).get(home::class.java)
        rv.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager
        }
        rvWeek.apply {
            adapter=mWeekAdapter
            layoutManager=mWeekLayoutManager
        }
        geocoder = Geocoder(requireContext(), Locale.getDefault())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val networkAvailability = NetworkAvailability()
        val isNetworkAvailable = networkAvailability.isNetworkAvailable(requireContext())
        if (isNetworkAvailable) {
            if (!favName.isNullOrEmpty()) {

            if (favLat != null) {
                if (favLon != null) {
                    allProductViewModel.getAllWeatherFromFav(favLat,favLon)
                    Log.i("TAGMap", "onCreateView: $favLat +   $favLon ")
                }
            }
            lifecycleScope.launch {
                allProductViewModel.weatherStateFlow.collectLatest {
                        result->
                    when(result){
                        is ApiState.loading->{
                            progressBar.visibility = ProgressBar.GONE
                            progressBar1.visibility = ProgressBar.VISIBLE
                            progressBar2.visibility = ProgressBar.VISIBLE
                            Log.i("TAG", "LOOOOODING: ")

                        }
                        is ApiState.Sucessed->{
                            progressBar.visibility = ProgressBar.GONE
                            progressBar1.visibility = ProgressBar.GONE
                            progressBar2.visibility = ProgressBar.GONE
                            mAdapter.setDataAndFilterByDate(result.data.list)
                            mWeekAdapter.setData(result.data.list)
                            updateUIFromFav(result.data,favName)
                        }
                        else->{
                            progressBar.visibility = ProgressBar.GONE
                            progressBar1.visibility = ProgressBar.GONE
                            progressBar2.visibility = ProgressBar.GONE

                        }


                    }
                }
            }
        }

        else{
            if(SharedPreferencesManager.getInstance(requireContext()).getLatitude() != 0.0f){
                allProductViewModel.getAllWeatherMap()
                lifecycleScope.launch {
                    allProductViewModel.weatherStateFlow.collectLatest {
                            result->
                        when(result){
                            is ApiState.loading->{
                                progressBar.visibility = ProgressBar.GONE
                                progressBar1.visibility = ProgressBar.VISIBLE
                                progressBar2.visibility = ProgressBar.VISIBLE
                                Log.i("TAG", "LOOOOODING: ")

                            }
                            is ApiState.Sucessed->{
                                progressBar.visibility = ProgressBar.GONE
                                progressBar1.visibility = ProgressBar.GONE
                                progressBar2.visibility = ProgressBar.GONE
                                mAdapter.setDataAndFilterByDate(result.data.list)
                                mWeekAdapter.setData(result.data.list)
                                updateUI(result.data)
                            }
                            else->{
                                progressBar.visibility = ProgressBar.GONE
                                progressBar1.visibility = ProgressBar.GONE
                                progressBar2.visibility = ProgressBar.GONE

                            }


                        }
                    }
                }
                SharedPreferencesManager.getInstance(requireContext()).clearLatitude()
            }
            else{
                allProductViewModel.getAllWeatherGps()
                lifecycleScope.launch {
                    allProductViewModel.weatherStateFlow.collectLatest {
                            result->
                        when(result){
                            is ApiState.loading->{
                                progressBar.visibility = ProgressBar.GONE
                                progressBar1.visibility = ProgressBar.VISIBLE
                                progressBar2.visibility = ProgressBar.VISIBLE
                                Log.i("TAG", "LOOOOODING: ")

                            }
                            is ApiState.Sucessed->{
                                progressBar.visibility = ProgressBar.GONE
                                progressBar1.visibility = ProgressBar.GONE
                                progressBar2.visibility = ProgressBar.GONE
                                mAdapter.setDataAndFilterByDate(result.data.list)
                                mWeekAdapter.setData(result.data.list)
                                updateUI(result.data)
                                allProductViewModel.insertHome(result.data)
                            }
                            is ApiState.fail -> {
                                // Hide loading indicator
                                // Show error message to user
                                val errorMessage = result.msg.message
                                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                            }
                            else->{
                                progressBar.visibility = ProgressBar.GONE
                                progressBar1.visibility = ProgressBar.GONE
                                progressBar2.visibility = ProgressBar.GONE
                        }


                        }
                    }
                }

            }
        }

    }else {
            Log.i("TAG", "nooo network: ")
            allProductViewModel.getStoredHome()
            lifecycleScope.launch {
                allProductViewModel.weatherStateFlow.collectLatest {
                        result->
                    when(result){
                        is ApiState.loading->{
                            progressBar.visibility = ProgressBar.GONE
                            progressBar1.visibility = ProgressBar.VISIBLE
                            progressBar2.visibility = ProgressBar.VISIBLE
                            Log.i("TAG", "LOOOOODING: ")

                        }
                        is ApiState.Sucess->{
                            progressBar.visibility = ProgressBar.GONE
                            progressBar1.visibility = ProgressBar.GONE
                            progressBar2.visibility = ProgressBar.GONE
                            mAdapter.setDataAndFilterByDate(result.data.last().list)
                            mWeekAdapter.setData(result.data.last().list)
                            updateUI(result.data.last())
                        }
                        else->{
                            progressBar.visibility = ProgressBar.GONE
                            progressBar1.visibility = ProgressBar.GONE
                            progressBar2.visibility = ProgressBar.GONE

                        }


                    }
                }
            }

        }
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.locale != Locale.getDefault()) {
            // Language configuration has changed, update locale
            Locale.setDefault(newConfig.locale)
            val context = requireContext()
            val resources = context.resources
            val configuration = Configuration(newConfig)
            configuration.locale = newConfig.locale

            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
    }

    private fun convertWindSpeedToKmh(windSpeed: Double): Double {
        return windSpeed * 3.6
    }

    private fun convertWindSpeedToMs(windSpeed: Double): Double {
        return windSpeed / 3.6
    }
    private fun updateUIFromFav(weatherForecast: Responce,city:String) {
        val rootView = view ?: return
        textViewCity.text=city
        val todayEntries = weatherForecast.list
        if (todayEntries.isNotEmpty()) {
            val todayWeather = todayEntries[0]
            var windString= "m/s"
            if (SharedPreferencesManager.getInstance(requireContext()).getLanguageUnit()=="ar") {
                when (SharedPreferencesManager.getInstance(requireContext()).getUnitWind()) {
                    "m/s" -> windString ="م/ث"
                    "km/h" -> windString = "كم/س"
                    else -> todayWeather.wind.speed}

            }else{
           when (SharedPreferencesManager.getInstance(requireContext()).getUnitWind()) {
                "m/s" -> windString = "m/s"
                "km/h" -> windString = "km/h"
                else -> todayWeather.wind.speed}
            }
            val windSpeed = when (SharedPreferencesManager.getInstance(requireContext()).getUnitWind()) {
                "m/s" -> convertWindSpeedToMs(todayWeather.wind.speed)
                "km/h" -> convertWindSpeedToKmh(todayWeather.wind.speed)
                else -> todayWeather.wind.speed
            }
            val formattedWindSpeed = String.format("%.2f", windSpeed)
            rootView.findViewById<TextView>(R.id.Wind)?.text = "${formattedWindSpeed} $windString"
            val pressursnumber = todayWeather.main.pressure
            val formattedpressursNumber= if (SharedPreferencesManager.getInstance(requireContext()).getLanguageUnit()=="ar") {
                val arabicNumber = convertNumber().convertNumberToArabic(pressursnumber)
                "$arabicNumber وحدة"
            } else {
                "$pressursnumber hpa"
            }
            rootView.findViewById<TextView>(R.id.pressurs)?.text = formattedpressursNumber
            val humiditynumber = todayWeather.main.humidity
            val formattedhumidityNumber= if (SharedPreferencesManager.getInstance(requireContext()).getLanguageUnit()=="ar") {
                val arabicNumber = convertNumber().convertNumberToArabic(humiditynumber)
                "$arabicNumber%"
            } else {
                "$humiditynumber %"
            }
            rootView.findViewById<TextView>(R.id.humidity)?.text=formattedhumidityNumber
            val cloudnumber = todayWeather.clouds.all
            val formattedCloudNumber= if (SharedPreferencesManager.getInstance(requireContext()).getLanguageUnit()=="ar") {
                val arabicNumber = convertNumber().convertNumberToArabic(cloudnumber)
                "$arabicNumber %"
            } else {
                "$cloudnumber %"
            }
            rootView.findViewById<TextView>(R.id.clouds)?.text=formattedCloudNumber
            var currentTemp="°C"
            if (SharedPreferencesManager.getInstance(requireContext()).getLanguageUnit()=="en"){
            when (SharedPreferencesManager.getInstance(requireContext()).getUnits()) {

                "metric" -> currentTemp = "°C"
                "imperial" -> currentTemp = "F"
                ""->currentTemp = "K"
                else -> "K"}}else{
                when (SharedPreferencesManager.getInstance(requireContext()).getUnits()) {

                    "metric" -> currentTemp = " سيليزي"
                    "imperial" -> currentTemp = " فهرنهايت"
                    ""->currentTemp = " كلفن"
                    else -> " كلفن"}
                }
            val feelsLikeNumber = todayWeather.main.feels_like.toInt()
            val feelsLikeText = getString(R.string.feels_like)
            val formattedFeelsLike = if (SharedPreferencesManager.getInstance(requireContext()).getLanguageUnit()=="ar") {
                val arabicNumber = convertNumber().convertNumberToArabic(feelsLikeNumber)
                "$feelsLikeText $arabicNumber $currentTemp"
            } else {
                "$feelsLikeText $feelsLikeNumber $currentTemp"
            }

            rootView.findViewById<TextView>(R.id.Feels_Like)?.text = formattedFeelsLike


            val tempNumber = todayWeather.main.temp.toInt()
            val formattedtempNumber= if (SharedPreferencesManager.getInstance(requireContext()).getLanguageUnit()=="ar") {
                val arabicNumber = convertNumber().convertNumberToArabic(tempNumber)
                "$arabicNumber $currentTemp"
            } else {
                "$tempNumber $currentTemp"
            }
            rootView.findViewById<TextView>(R.id.textViewTemperature)?.text =formattedtempNumber
            val iconCode = todayWeather.weather[0].icon
            val icon=IconUrl()
            val iconUrl = icon.getIconDrawable(iconCode,requireContext())
            val imageViewWeatherIcon = rootView.findViewById<ImageView>(R.id.imageViewWeatherIcon)
            imageViewWeatherIcon?.let {
                Glide.with(requireContext())
                    .load(iconUrl)
                    .listener(object : RequestListener<Drawable> {

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            val rotateAnimation = RotateAnimation(-10f, 10f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                            rotateAnimation.duration = 1000 // 1 second
                            rotateAnimation.interpolator = LinearInterpolator()
                            rotateAnimation.repeatCount = Animation.INFINITE
                            imageViewWeatherIcon.startAnimation(rotateAnimation)
                            return false
                        }

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.e("TAG", "Error loading image", e)
                            return false                        }

                    })
                    .into(it)
            }
        } else {
            Log.i("TAG", "updateUI: No data available")
        }
    }


    private fun updateUI(weatherForecast: Responce) {
        val rootView = view ?: return
        Log.i("City", "updateUI:  ${weatherForecast.city.name.toString()} ")
        if(SharedPreferencesManager.getInstance(requireContext()).getLatitude() != 0.0f) {
            val addresses: List<Address>? =
                geocoder.getFromLocation(
                    SharedPreferencesManager.getInstance(requireContext()).getLatitude().toDouble(), SharedPreferencesManager.getInstance(requireContext()).getLongitude().toDouble(), 1)
            if (addresses?.isNotEmpty() == true) {
                    val cityName = addresses[0].getAddressLine(0)
                    textViewCity.text=weatherForecast.city.name.toString()
            }
        }
        else{
             textViewCity.text=weatherForecast.city.name.toString()}
        val todayEntries = weatherForecast.list
        if (todayEntries.isNotEmpty()) {
            val todayWeather = todayEntries[0]
            var windString= "m/s"
            if (SharedPreferencesManager.getInstance(requireContext()).getLanguageUnit()=="ar") {
                when (SharedPreferencesManager.getInstance(requireContext()).getUnitWind()) {
                    "m/s" -> windString ="م/ث"
                    "km/h" -> windString = "كم/س"
                    else -> todayWeather.wind.speed}

            }else{
                when (SharedPreferencesManager.getInstance(requireContext()).getUnitWind()) {
                    "m/s" -> windString = "m/s"
                    "km/h" -> windString = "km/h"
                    else -> todayWeather.wind.speed}
            }
            val windSpeed = when (SharedPreferencesManager.getInstance(requireContext()).getUnitWind()) {
                "m/s" -> convertWindSpeedToMs(todayWeather.wind.speed)
                "km/h" -> convertWindSpeedToKmh(todayWeather.wind.speed)
                else -> todayWeather.wind.speed
            }
            val formattedWindSpeed = String.format("%.2f", windSpeed)
            rootView.findViewById<TextView>(R.id.Wind)?.text = "${formattedWindSpeed} $windString"

            val pressursnumber = todayWeather.main.pressure
            val formattedpressursNumber= if (SharedPreferencesManager.getInstance(requireContext()).getLanguageUnit()=="ar") {
                val arabicNumber = convertNumber().convertNumberToArabic(pressursnumber)
                "$arabicNumber وحدة"
            } else {
                "$pressursnumber hpa"
            }
            rootView.findViewById<TextView>(R.id.pressurs)?.text = formattedpressursNumber
            val humiditynumber = todayWeather.main.humidity
            val formattedhumidityNumber= if (SharedPreferencesManager.getInstance(requireContext()).getLanguageUnit()=="ar") {
                val arabicNumber = convertNumber().convertNumberToArabic(humiditynumber)
                "$arabicNumber%"
            } else {
                "$humiditynumber %"
            }
            rootView.findViewById<TextView>(R.id.humidity)?.text=formattedhumidityNumber
            val cloudnumber = todayWeather.clouds.all
            val formattedCloudNumber= if (SharedPreferencesManager.getInstance(requireContext()).getLanguageUnit()=="ar") {
                val arabicNumber = convertNumber().convertNumberToArabic(cloudnumber)
                "$arabicNumber %"
            } else {
                "$cloudnumber %"
            }
            rootView.findViewById<TextView>(R.id.clouds)?.text=formattedCloudNumber
            var currentTemp="°C"
            if (SharedPreferencesManager.getInstance(requireContext()).getLanguageUnit()=="en"){
                when (SharedPreferencesManager.getInstance(requireContext()).getUnits()) {

                    "metric" -> currentTemp = "°C"
                    "imperial" -> currentTemp = "F"
                    ""->currentTemp = "K"
                    else -> "K"}}else{
                when (SharedPreferencesManager.getInstance(requireContext()).getUnits()) {

                    "metric" -> currentTemp = " سيليزي"
                    "imperial" -> currentTemp = " فهرنهايت"
                    ""->currentTemp = " كلفن"
                    else -> " كلفن"}
            }

            val feelsLikeNumber = todayWeather.main.feels_like.toInt()
            val feelsLikeText = getString(R.string.feels_like)
            val formattedFeelsLike = if (SharedPreferencesManager.getInstance(requireContext()).getLanguageUnit()=="ar") {
                val arabicNumber = convertNumber().convertNumberToArabic(feelsLikeNumber)
                "$feelsLikeText $arabicNumber $currentTemp"
            } else {
                "$feelsLikeText $feelsLikeNumber $currentTemp"
            }
            rootView.findViewById<TextView>(R.id.Feels_Like)?.text = formattedFeelsLike
            val tempNumber = todayWeather.main.temp.toInt()
            val formattedtempNumber= if (SharedPreferencesManager.getInstance(requireContext()).getLanguageUnit()=="ar") {
                val arabicNumber = convertNumber().convertNumberToArabic(tempNumber)
                "$arabicNumber $currentTemp"
            } else {
                "$tempNumber $currentTemp"
            }
            rootView.findViewById<TextView>(R.id.textViewTemperature)?.text =formattedtempNumber

            val iconCode = todayWeather.weather[0].icon
            val icon=IconUrl()
            val iconUrl = icon.getIconDrawable(iconCode,requireContext())
            val imageViewWeatherIcon = rootView.findViewById<ImageView>(R.id.imageViewWeatherIcon)
            imageViewWeatherIcon?.let {
                Glide.with(requireContext())
                    .load(iconUrl)
                    .listener(object : RequestListener<Drawable> {

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            val rotateAnimation = RotateAnimation(-10f, 10f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                            rotateAnimation.duration = 1000 // 1 second
                            rotateAnimation.interpolator = LinearInterpolator()
                            rotateAnimation.repeatCount = Animation.INFINITE
                            imageViewWeatherIcon.startAnimation(rotateAnimation)
                            return false
                        }

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.e("TAG", "Error loading image", e)
                            return false                        }

                    })
                    .into(it)
            }
        } else {
            Log.i("TAG", "updateUI: No data available")
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
                    textViewCity.text = "  $city  "
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

    private fun getLastLocation() {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        updateLocationTextView(it)
                        reverseGeocode(it)
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
