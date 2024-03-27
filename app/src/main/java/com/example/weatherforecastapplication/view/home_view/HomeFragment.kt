package com.example.weatherforecastapplication.view.home_view

import WeatherLocalDataSourceImp
import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.model2.RepositoryImp
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
import com.example.weatherforecastapplication.network.RemoteDataSourceImp
import com.example.weatherforecastapplication.model2.Responce
import com.example.weatherforecastapplication.network.ApiState
import com.example.weatherforecastapplication.view.NetworkAvailability
import com.example.weatherforecastapplication.view_model.Fav
import com.example.weatherforecastapplication.view_model.FavFactory
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
   lateinit var allFavViewModel:Fav
    lateinit var allFavFactroy: FavFactory
    lateinit var FeelsLike:TextView
    val LOCATION_PERMISSION_REQUEST_CODE = 10
    lateinit var mAdapter: HomeAdapter
    lateinit var mWeekAdapter: HomeWeekAdapter
    lateinit var mWeekLayoutManager: LinearLayoutManager

    lateinit var rv: RecyclerView
    lateinit var rvWeek: RecyclerView
    lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var textViewCity: TextView
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
        Log.i("TAGMap", "OnCLickIteamFav:$favLat + $favLon  ")
        mWeekLayoutManager= LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
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

//           allProductViewModel.getAllWeatherGps()
            requestLocationPermission()
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
                            //    progressBar.visibility = ProgressBar.VISIBLE
                            Log.i("TAG", "LOOOOODING: ")

                        }
                        is ApiState.Sucessed->{
                            //     progressBar.visibility = ProgressBar.GONE
                            mAdapter.setDataAndFilterByDate(result.data.list)
                            mWeekAdapter.setData(result.data.list)
                            updateUIFromFav(result.data,favName)
                        }
                        else->{
                            //   progressBar.visibility = ProgressBar.GONE

                            Toast.makeText(requireContext(), "Failed to load data favName", Toast.LENGTH_SHORT).show()

                        }


                    }
                }
            }


//            allProductViewModel.Favproducts.observe(viewLifecycleOwner,
//                Observer<Responce> { value ->
//                    mAdapter.setDataAndFilterByDate(value.list)
//                    mWeekAdapter.setData(value.list)
//                    updateUIFromFav(value,favName)
//                })
        }

        else{
            if(SharedPreferencesManager.getInstance(requireContext()).getLatitude() != 0.0f){
                allProductViewModel.getAllWeatherMap()
                lifecycleScope.launch {
                    allProductViewModel.weatherStateFlow.collectLatest {
                            result->
                        when(result){
                            is ApiState.loading->{
                                //    progressBar.visibility = ProgressBar.VISIBLE
                                Log.i("TAG", "LOOOOODING: ")

                            }
                            is ApiState.Sucessed->{
                                //     progressBar.visibility = ProgressBar.GONE
                                mAdapter.setDataAndFilterByDate(result.data.list)
                                mWeekAdapter.setData(result.data.list)
                                updateUI(result.data)
                            }
                            else->{
                                //   progressBar.visibility = ProgressBar.GONE

                                Toast.makeText(requireContext(), "Failed to load data getAllWeatherMap", Toast.LENGTH_SHORT).show()

                            }


                        }
                    }
                }





//                allProductViewModel.products.observe(viewLifecycleOwner,
//                    Observer<Responce> { value ->
//                        mAdapter.setDataAndFilterByDate(value.list)
//                        mWeekAdapter.setData(value.list)
//                        updateUI(value)
//                    })
                SharedPreferencesManager.getInstance(requireContext()).clearLatitude()
            }
            else{
                allProductViewModel.getAllWeatherGps()
                lifecycleScope.launch {
                    allProductViewModel.weatherStateFlow.collectLatest {
                            result->
                        when(result){
                            is ApiState.loading->{
                                //    progressBar.visibility = ProgressBar.VISIBLE
                                Log.i("TAG", "LOOOOODING: ")

                            }
                            is ApiState.Sucessed->{
                                //    progressBar.visibility = ProgressBar.GONE
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
                            else->{Toast.makeText(requireContext(), "errorMessage", Toast.LENGTH_SHORT).show()
                        }


                        }
                    }
                }

//                allProductViewModel.products.observe(viewLifecycleOwner,
//                    Observer<Responce> { value ->
//                        mAdapter.setDataAndFilterByDate(value.list)
//                        mWeekAdapter.setData(value.list)
//                        updateUI(value)
//                        allProductViewModel.insertHome(value)
//                    })
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
                        //    progressBar.visibility = ProgressBar.VISIBLE
                            Log.i("TAG", "LOOOOODING: ")

                        }
                        is ApiState.Sucess->{
                       //     progressBar.visibility = ProgressBar.GONE

                            mAdapter.setDataAndFilterByDate(result.data.last().list)
                            mWeekAdapter.setData(result.data.last().list)
                            updateUI(result.data.last())
                        }
                        else->{
                         //   progressBar.visibility = ProgressBar.GONE

                            Toast.makeText(requireContext(), "Failed to load data nooo net", Toast.LENGTH_SHORT).show()

                        }


                    }
                }
            }
//            allProductViewModel.weatherHome.observe(viewLifecycleOwner,
//                Observer<List<Responce>> { value ->
//                    Log.i("TAG", "o $value")
//                    mAdapter.setDataAndFilterByDate(value[0].list)
//                    mWeekAdapter.setData(value[0].list)
//                    updateUI(value[0])
//                })

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
            rootView.findViewById<TextView>(R.id.Feels_Like)?.text=
                "Feels Like :  ${ todayWeather.main.feels_like.toInt().toString() }"
            val windSpeed = when (SharedPreferencesManager.getInstance(requireContext()).getUnitWind()) {
                "m/s" -> convertWindSpeedToMs(todayWeather.wind.speed)
                "km/h" -> convertWindSpeedToKmh(todayWeather.wind.speed)
                else -> todayWeather.wind.speed // Default to original value if no unit is found
            }
            rootView.findViewById<TextView>(R.id.Wind)?.text = "${windSpeed.toInt().toString()}"
            rootView.findViewById<TextView>(R.id.pressurs)?.text=todayWeather.main.pressure.toString()
            rootView.findViewById<TextView>(R.id.humidity)?.text=
                "${todayWeather.main.humidity.toString()} %"
            rootView.findViewById<TextView>(R.id.clouds)?.text=
                "${ todayWeather.clouds.all.toString()} %"
            rootView.findViewById<TextView>(R.id.textViewTemperature)?.text =
                "${todayWeather.main.temp.toInt()}°C"

            val iconCode = todayWeather.weather[0].icon
            val iconUrl = getIconUrl(iconCode)

            val imageViewWeatherIcon = rootView.findViewById<ImageView>(R.id.imageViewWeatherIcon)
            imageViewWeatherIcon?.let {
                Glide.with(requireContext())
                    .load(iconUrl)
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

            rootView.findViewById<TextView>(R.id.Feels_Like)?.text=
          "Feels Like :  ${ todayWeather.main.feels_like.toInt().toString() }"

            val windSpeed = when (SharedPreferencesManager.getInstance(requireContext()).getUnitWind()) {
                "m/s" -> convertWindSpeedToMs(todayWeather.wind.speed)
                "km/h" -> convertWindSpeedToKmh(todayWeather.wind.speed)
                else -> todayWeather.wind.speed // Default to original value if no unit is found
            }

            rootView.findViewById<TextView>(R.id.Wind)?.text = "${windSpeed.toInt().toString()}"

//            rootView.findViewById<TextView>(R.id.Wind)?.text=
//                todayWeather.wind.speed.toString()
            rootView.findViewById<TextView>(R.id.pressurs)?.text=todayWeather.main.pressure.toString()
            rootView.findViewById<TextView>(R.id.humidity)?.text=
                 "${todayWeather.main.humidity.toString()} %"
            rootView.findViewById<TextView>(R.id.clouds)?.text=
             "${ todayWeather.clouds.all.toString()} %"
            rootView.findViewById<TextView>(R.id.textViewTemperature)?.text =
                "${todayWeather.main.temp.toInt()}°C"

            val iconCode = todayWeather.weather[0].icon
            val iconUrl = getIconUrl(iconCode)

            val imageViewWeatherIcon = rootView.findViewById<ImageView>(R.id.imageViewWeatherIcon)
            imageViewWeatherIcon?.let {
                Glide.with(requireContext())
                    .load(iconUrl)
                    .into(it)
            }
        } else {
            Log.i("TAG", "updateUI: No data available")
        }
    }

    private fun getIconUrl(iconCode: String): Int {
        return when (iconCode) {
            "01d" -> R.drawable.day_forecast_sun_sunny_weather_icon
            "01n" -> R.drawable.eclipse_forecast_moon_night_space_icon
            "02d" -> R.drawable.cloud_cloudy_day_forecast_sun_icon
            "02n" -> R.drawable.weather_clouds_cloudy_moon_icon
            "03d", "03n" -> R.drawable.weather_cloud_clouds_cloudy_icon
            "04d", "04n" -> R.drawable.weather_cloud_clouds_cloudyy_icon
            "09d", "09n" -> R.drawable.clouds_cloudy_foggy_weather_icon
            "10d" -> R.drawable.weather_clouds_cloudy_forecast_rain_icon
            "10n" -> R.drawable.weather_clouds_cloudy_rain_sunny_icon
            "11d", "11n" -> R.drawable.cloud_flash_weather_rain_snow_icon
            "13d", "13n" -> R.drawable.weather_storm_icon
            "50d", "50n" -> R.drawable.rain_snowflake_snow_cloud_winter_icon
            else -> R.drawable.cloud_white_24dp // Default icon for unknown weather conditions
        }
    }


//    private fun getIconUrl(iconCode: String): String {
//        return "https://openweathermap.org/img/w/$iconCode.png"
//    }



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
        latitude = location.latitude
        longitude = location.longitude
    }






}