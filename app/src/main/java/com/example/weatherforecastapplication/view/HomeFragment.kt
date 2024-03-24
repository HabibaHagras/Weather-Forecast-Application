package com.example.weatherforecastapplication

import WeatherLocalDataSourceImp
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecastapplication.model2.RepositoryImp
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
import com.example.weatherforecastapplication.model2.WeatherData
import com.example.weatherforecastapplication.network.RemoteDataSourceImp
import com.example.weatherforecastapplication.model2.Responce
import com.example.weatherforecastapplication.network.ApiState
import com.example.weatherforecastapplication.view.HomeAdapter
import com.example.weatherforecastapplication.view.HomeWeekAdapter
import com.example.weatherforecastapplication.view_model.Fav
import com.example.weatherforecastapplication.view_model.FavFactory
import com.example.weatherforecastapplication.view_model.home
import com.example.weatherforecastapplication.view_model.homeFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
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
    lateinit var mWeekAdapter:HomeWeekAdapter
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
//        FeelsLike=rootView.findViewById(R.id.Feels_Like)
//        textViewCity = rootView.findViewById(R.id.textViewCity)
//        rv = rootView.findViewById(R.id.rv)
//        rvWeek = rootView.findViewById(R.id.rv_Week)
//        val favName = arguments?.getString("selected_city")
//        val favLat = arguments?.getDouble("selected_lat")
//        val favLon = arguments?.getDouble("selected_lon")
//        Log.i("TAGMap", "OnCLickIteamFav:$favLat + $favLon  ")
//
//        mWeekLayoutManager= LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//        mLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//        mAdapter = HomeAdapter(requireContext())
//        mWeekAdapter = HomeWeekAdapter(requireContext())
//        allProductFactroy = homeFactory(
//            RepositoryImp.getInstance(
//                RemoteDataSourceImp.getInstance(),WeatherLocalDataSourceImp(requireContext())
//            ), SharedPreferencesManager.getInstance(requireContext()
//            ))
//        allProductViewModel = ViewModelProvider(this, allProductFactroy).get(home::class.java)
//        rv.apply {
//            adapter = mAdapter
//            layoutManager = mLayoutManager
//        }
//        rvWeek.apply {
//            adapter=mWeekAdapter
//            layoutManager=mWeekLayoutManager
//        }
//
//        geocoder = Geocoder(requireContext(), Locale.getDefault())
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
//
//        requestLocationPermission()
//        if (!favName.isNullOrEmpty()) {
////            allFavFactroy = FavFactory(
////                RepositoryImp.getInstance(
////                    RemoteDataSourceImp.getInstance(),WeatherLocalDataSourceImp(requireContext())
////                ),favName
////            )
////            allFavViewModel = ViewModelProvider(this, allFavFactroy).get(Fav::class.java)
////
////            allFavViewModel.products.observe(viewLifecycleOwner,
////                Observer<WeatherData> { value ->
////                    updateUI2(value)
////                })
//            if (favLat != null) {
//                if (favLon != null) {
//                    allProductViewModel.getAllWeatherFromFav(favLat,favLon)
//                    Log.i("TAGMap", "onCreateView: $favLat +   $favLon ")
//                }
//            }
//            allProductViewModel.Favproducts.observe(viewLifecycleOwner,
//                Observer<Responce> { value ->
//                    mAdapter.setDataAndFilterByDate(value.list)
//                    mWeekAdapter.setData(value.list)
//                    updateUIFromFav(value,favName)
//                })
//        }
//
//        else{
////            allProductFactroy = homeFactory(
////                RepositoryImp.getInstance(
////                    RemoteDataSourceImp.getInstance(),WeatherLocalDataSourceImp(requireContext())
////                ), SharedPreferencesManager.getInstance(requireContext()
////            ))
////            allProductViewModel = ViewModelProvider(this, allProductFactroy).get(home::class.java)
//            allProductViewModel.getAllProducts()
//            allProductViewModel.products.observe(viewLifecycleOwner,
//                Observer<Responce> { value ->
//                    mAdapter.setDataAndFilterByDate(value.list)
//                    mWeekAdapter.setData(value.list)
//                    updateUI(value)
//                })
//        }

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
        if (isNetworkAvailable()) {

//           allProductViewModel.getAllWeatherGps()
            requestLocationPermission()
        if (!favName.isNullOrEmpty()) {

            if (favLat != null) {
                if (favLon != null) {
                    allProductViewModel.getAllWeatherFromFav(favLat,favLon)
                    Log.i("TAGMap", "onCreateView: $favLat +   $favLon ")
                }
            }
            allProductViewModel.Favproducts.observe(viewLifecycleOwner,
                Observer<Responce> { value ->
                    mAdapter.setDataAndFilterByDate(value.list)
                    mWeekAdapter.setData(value.list)
                    updateUIFromFav(value,favName)
                })
        }

        else{
            if(SharedPreferencesManager.getInstance(requireContext()).getLatitude() != 0.0f){
                allProductViewModel.getAllWeatherMap()

                allProductViewModel.products.observe(viewLifecycleOwner,
                    Observer<Responce> { value ->
                        mAdapter.setDataAndFilterByDate(value.list)
                        mWeekAdapter.setData(value.list)
                        updateUI(value)
                    })
                SharedPreferencesManager.getInstance(requireContext()).clearLatitude()
            }
            else{
                allProductViewModel.getAllWeatherGps()

                allProductViewModel.products.observe(viewLifecycleOwner,
                    Observer<Responce> { value ->
                        mAdapter.setDataAndFilterByDate(value.list)
                        mWeekAdapter.setData(value.list)
                        updateUI(value)
                        allProductViewModel.insertHome(value)
                    })
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

                            Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()

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
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
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
        val rootView = view ?: return  // Check if the root view is null
        textViewCity.text=weatherForecast.city.name.toString()
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



    private fun getIconUrl(iconCode: String): String {
        return "https://openweathermap.org/img/w/$iconCode.png"
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
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
        latitude = location.latitude
        longitude = location.longitude
    }
}
