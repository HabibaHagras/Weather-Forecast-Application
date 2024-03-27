package com.example.weatherforecastapplication.view

import WeatherLocalDataSourceImp
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.HomeFragment
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.model2.RepositoryImp
import com.example.weatherforecastapplication.model2.WeatherData
import com.example.weatherforecastapplication.network.ApiState
import com.example.weatherforecastapplication.network.RemoteDataSourceImp
import com.example.weatherforecastapplication.view_model.Fav
import com.example.weatherforecastapplication.view_model.FavFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavFragment : Fragment(), FavListener {
    private lateinit var fab: FloatingActionButton
    lateinit var allFavViewModel: Fav
    lateinit var allFavFactroy: FavFactory
    private lateinit var adapter: FavAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fav, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = FavAdapter(FavFragment(), this)
        allFavFactroy = FavFactory(
            RepositoryImp.getInstance(
                RemoteDataSourceImp.getInstance(),
                WeatherLocalDataSourceImp(requireContext())
            ), "Tanta"
        )
        allFavViewModel = ViewModelProvider(this, allFavFactroy).get(Fav::class.java)
        recyclerView.adapter = adapter

        allFavViewModel.productsw.observe(viewLifecycleOwner, Observer { weatherDataList ->
            adapter.setData(weatherDataList)

        })
        lifecycleScope.launch {
            allFavViewModel.weatherStateFlow.collectLatest {
                    result->
                when(result){
                    is ApiState.loading->{
                        //    progressBar.visibility = ProgressBar.VISIBLE
                        Log.i("TAG", "LOOOOODING Fav: ")

                    }
                    is ApiState.SucessWeatherData->{
                        //     progressBar.visibility = ProgressBar.GONE
                        adapter.setData(result.data)
                    }
                    else->{
                        //   progressBar.visibility = ProgressBar.GONE

                        Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT)

                    }


                }
            }
        }


        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener {
//            showAddCityDialog()
            val intent = Intent(requireContext(), MapsActivity::class.java).apply {
                putExtra("favorite",0) // Replace latitudeValue with the actual latitude
            }
            startActivity(intent)
            //startActivity(Intent(requireContext(), MapsActivity::class.java))

//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, NotificationFragment())
//                .addToBackStack(null)
//                .commit()
        }

        return view
    }
    override fun onResume() {
        super.onResume()
        refreshFavorites()
    }

    override fun onStop() {
        super.onStop()
        Log.i("TAG", "onStop: ")
        refreshFavorites()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("TAG", "onViewCreated:  ")

        refreshFavorites()

    }

    override fun onStart() {
        super.onStart()
        Log.i("TAG", "onStart: ")
        allFavViewModel.getStored()
        refreshFavorites()

    }
    private fun refreshFavorites() {
        allFavViewModel.productsw.observe(viewLifecycleOwner, Observer { weatherDataList ->
            adapter.setData(weatherDataList)
            adapter.notifyDataSetChanged()

        })
    }
    private fun showNoNetworkDialog() {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.fav_city_dialog, null)
//        val editTextFavCity = dialogView.findViewById<EditText>(R.id.edit_text_fav_city)
//        val saveButton = dialogView.findViewById<Button>(R.id.button_save)

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Add Favorite City")
        val alertDialog = dialogBuilder.create()

//        saveButton.setOnClickListener {
//            val cityName = editTextFavCity.text.toString().trim()
//            if (cityName.isNotEmpty()) {
//                alertDialog.dismiss()
//            } else {
//                // Handle empty city name
//                editTextFavCity.error = "Please enter a city name"
//            }
//        }

        alertDialog.show()
    }

    override fun OnCLickIteamFav(lat: Double ,lon:Double,city:String) {
        val networkAvailability = NetworkAvailability()
        val isNetworkAvailable = networkAvailability.isNetworkAvailable(requireContext())
        if (isNetworkAvailable) {
            val anotherFragment = HomeFragment()
            val bundle = Bundle().apply {
                Log.i("TAGMap", "OnCLickIteamFav:$lat + $lon  ")
                putDouble("selected_lat", lat)
                putDouble("selected_lon", lon)
                putString("selected_city", city)

            }
            anotherFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, anotherFragment)
                .addToBackStack(null)
                .commit()
        }else {
            showNoNetworkDialog()

        }
    }

    override fun deleteIteamFav(weather: WeatherData) {
        Log.i("TAG", "deleteIteamFavvvvvvvvvvvvvvvvvvvvvvvvvvvvv: ")
        allFavViewModel.deleteWeather(weather)
        refreshFavorites()

    }
}