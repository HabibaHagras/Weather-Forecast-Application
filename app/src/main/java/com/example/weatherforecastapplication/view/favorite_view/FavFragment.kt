package com.example.weatherforecastapplication.view.favorite_view

import WeatherLocalDataSourceImp
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.view.home_view.HomeFragment
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.model2.RepositoryImp
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
import com.example.weatherforecastapplication.model2.WeatherData
import com.example.weatherforecastapplication.network.ApiState
import com.example.weatherforecastapplication.network.RemoteDataSourceImp
import com.example.weatherforecastapplication.view.MapsActivity
import com.example.weatherforecastapplication.view.NetworkAvailability
import com.example.weatherforecastapplication.view_model.Fav
import com.example.weatherforecastapplication.view_model.FavFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [FavFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavFragment : Fragment(), FavListener ,SearchListener {
    private lateinit var fab: FloatingActionButton
    lateinit var allFavViewModel: Fav
    lateinit var allFavFactroy: FavFactory
    private lateinit var adapter: FavAdapter
    lateinit var recyclerViewCities: RecyclerView
    lateinit var recyclerView: RecyclerView
    lateinit var searchBar: EditText
    var sharedFlow = MutableSharedFlow<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fav, container, false)
         recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = FavAdapter(FavFragment(), this)
        searchBar = view.findViewById(R.id.searchEditText)
        recyclerViewCities = view.findViewById(R.id.rv_names)
        allFavFactroy = FavFactory(
            RepositoryImp.getInstance(
                RemoteDataSourceImp.getInstance(),
                WeatherLocalDataSourceImp(requireContext())
            ), "Tanta", SharedPreferencesManager.getInstance(requireContext())
        )
        allFavViewModel = ViewModelProvider(this, allFavFactroy).get(Fav::class.java)
        recyclerView.adapter = adapter
        lifecycleScope.launch {
            allFavViewModel.weatherStateFlow.collectLatest { result ->
                when (result) {
                    is ApiState.loading -> {
                        //    progressBar.visibility = ProgressBar.VISIBLE
                        Log.i("TAG", "LOOOOODING Fav: ")

                    }

                    is ApiState.SucessWeatherData -> {
                        //     progressBar.visibility = ProgressBar.GONE
                        adapter.setData(result.data)
                    }

                    else -> {
                        //   progressBar.visibility = ProgressBar.GONE

                        Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT)

                    }


                }
            }
        }


        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(requireContext(), MapsActivity::class.java).apply {
                putExtra("favorite", 0) // Replace latitudeValue with the actual latitude
            }
            startActivity(intent)

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


        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString().trim()
                if (searchText.isEmpty()) {
                    recyclerViewCities.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    fab.visibility = View.VISIBLE

                } else {
                    recyclerViewCities.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    fab.visibility = View.GONE


                    lifecycleScope.launch {
                        sharedFlow.emit(searchText)
                    }
                }
            }
        })
        val cities = listOf<String>("Tanta", "Cairo", "London")
        val searchAdapter = SearchAdapter(requireContext(), this)

        lifecycleScope.launch {
            sharedFlow
                .distinctUntilChanged()
                .collect() {
                    var newNames = cities.filter { name ->
                        name.contains(it)
                    }
                    recyclerViewCities.apply {
                        layoutManager = LinearLayoutManager(this.context).apply {
                            orientation = LinearLayoutManager.VERTICAL
                        }
                        adapter = searchAdapter.apply {
                            submitList(newNames)


                        }
                    }
                }

        }
    }


    override fun onStart() {
        super.onStart()
        Log.i("TAG", "onStart: ")
        allFavViewModel.getStored()
        refreshFavorites()

    }

    private fun refreshFavorites() {

        lifecycleScope.launch {
            allFavViewModel.weatherStateFlow.collectLatest { result ->
                when (result) {
                    is ApiState.loading -> {
                        //    progressBar.visibility = ProgressBar.VISIBLE
                        Log.i("TAG", "LOOOOODING Fav: ")

                    }

                    is ApiState.SucessWeatherData -> {
                        //     progressBar.visibility = ProgressBar.GONE
                        adapter.setData(result.data)
                    }

                    else -> {
                        //   progressBar.visibility = ProgressBar.GONE

                        Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT)

                    }


                }
            }
        }
    }

    private fun showNoNetworkDialog() {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.fav_city_dialog, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("No Network Connection ...")
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    override fun OnCLickIteamFav(lat: Double, lon: Double, city: String) {
        val networkAvailability = NetworkAvailability()
        val isNetworkAvailable = networkAvailability.isNetworkAvailable(requireContext())
        if (isNetworkAvailable) {
            val anotherFragment = HomeFragment()
            val bundle = Bundle().apply {
                putDouble("selected_lat", lat)
                putDouble("selected_lon", lon)
                putString("selected_city", city)
            }
            anotherFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, anotherFragment)
                .addToBackStack(null)
                .commit()
        } else {
            showNoNetworkDialog()

        }
    }

    override fun deleteIteamFav(weather: WeatherData) {
        Log.i("TAG", "deleteIteamFavvvvvvvvvvvvvvvvvvvvvvvvvvvvv: ")
        allFavViewModel.deleteWeather(weather)
        refreshFavorites()

    }

    override fun onCitySelected(cityName: String) {
        recyclerViewCities.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        fab.visibility = View.VISIBLE
        allFavViewModel.getAllWeather(cityName)
        lifecycleScope.launch {
            allFavViewModel.weatherStateFlow.collectLatest { result ->
                when (result) {
                    is ApiState.loading -> {
                        //    progressBar.visibility = ProgressBar.VISIBLE
                        Log.i("TAG", "LOOOOODING Fav: ")

                    }

                    is ApiState.SucessedWeather -> {
                        //     progressBar.visibility = ProgressBar.GONE
                        allFavViewModel.insertProducts(result.data)
                    }

                    else -> {
                        //   progressBar.visibility = ProgressBar.GONE

                        Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT)

                    }


                }
            }
        }
        refreshFavorites()

    }}


