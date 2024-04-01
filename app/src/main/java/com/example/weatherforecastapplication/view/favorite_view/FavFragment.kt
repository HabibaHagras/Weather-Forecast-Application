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
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentFavBinding
import com.example.weatherforecastapplication.model2.RepositoryImp
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
import com.example.weatherforecastapplication.model2.WeatherData
import com.example.weatherforecastapplication.network.ApiState
import com.example.weatherforecastapplication.network.RemoteDataSourceImp
import com.example.weatherforecastapplication.view.MapsActivity
import com.example.weatherforecastapplication.view.NetworkAvailability
import com.example.weatherforecastapplication.view.home_view.HomeFragment
import com.example.weatherforecastapplication.view_model.Fav
import com.example.weatherforecastapplication.view_model.FavFactory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class FavFragment : Fragment(), FavListener, SearchListener {
    private lateinit var binding: FragmentFavBinding
    private lateinit var allFavViewModel: Fav
    private lateinit var allFavFactory: FavFactory
    private lateinit var adapter: FavAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var sharedFlow: MutableSharedFlow<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        searchBar = binding.searchEditText

        adapter = FavAdapter(FavFragment(), this)

        allFavFactory = FavFactory(
            RepositoryImp.getInstance(
                RemoteDataSourceImp.getInstance(),
                WeatherLocalDataSourceImp(requireContext())
            ), "Tanta", SharedPreferencesManager.getInstance(requireContext())
        )

        allFavViewModel = ViewModelProvider(this, allFavFactory).get(Fav::class.java)
        recyclerView.adapter = adapter

        sharedFlow = MutableSharedFlow()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            allFavViewModel.weatherStateFlow.collectLatest { result ->
                when (result) {
                    is ApiState.loading -> {
                        Log.i("TAG", "LOOOOODING Fav: ")
                    }
                    is ApiState.SucessWeatherData -> {
                        binding.ConstraintLayout1.visibility = View.GONE
                        adapter.setData(result.data)
                        if (result.data.isEmpty()) {
                            binding.ConstraintLayout1.visibility = View.VISIBLE
                            Log.i("TAG", "Favorites list is empty")
                        }
                    }
                    else -> {
                    }
                }
            }
        }

        binding.fab.setOnClickListener {
            val networkAvailability = NetworkAvailability()
            val isNetworkAvailable = networkAvailability.isNetworkAvailable(requireContext())
            if (isNetworkAvailable) {
                val intent = Intent(requireContext(), MapsActivity::class.java).apply {
                    putExtra("favorite", 0)
                }
                startActivity(intent)
            } else {
                showNoNetworkDialog()
            }
        }

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString().trim()
                if (searchText.isEmpty()) {
                    binding.rvNames.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    binding.fab.visibility = View.VISIBLE
                } else {
                    binding.rvNames.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    binding.fab.visibility = View.GONE
                    lifecycleScope.launch {
                        sharedFlow.emit(searchText)
                    }
                }
            }
        })

        val cities = listOf<String>("Tanta", "Cairo", "London","Paris")
        val searchAdapter = SearchAdapter(requireContext(), this)
        lifecycleScope.launch {
            sharedFlow.distinctUntilChanged().collect { searchText ->
                val newNames = cities.filter { name ->
                    name.contains(searchText)
                }
                binding.rvNames.apply {
                    layoutManager = LinearLayoutManager(context).apply {
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
        allFavViewModel.getStored()
        refreshFavorites()
    }

    override fun onResume() {
        super.onResume()
        refreshFavorites()
    }

    override fun onStop() {
        super.onStop()
        refreshFavorites()
    }

    private fun refreshFavorites() {
        lifecycleScope.launch {
            allFavViewModel.weatherStateFlow.collectLatest { result ->
                when (result) {
                    is ApiState.loading -> {
                        Log.i("TAG", "LOOOOODING Fav: ")
                    }
                    is ApiState.SucessWeatherData -> {
                        adapter.setData(result.data)
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun showNoNetworkDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.fav_city_dialog, null)
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
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Confirm Deletion")
        alertDialogBuilder.setMessage("Are you sure you want to delete this item?")
        alertDialogBuilder.setPositiveButton("Yes") { dialog, _ ->
            allFavViewModel.deleteWeather(weather)
            refreshFavorites()
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onCitySelected(cityName: String) {
        binding.rvNames.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        binding.fab.visibility = View.VISIBLE
        allFavViewModel.getAllWeather(cityName)
        lifecycleScope.launch {
            allFavViewModel.weatherStateFlow.collectLatest { result ->
                when (result) {
                    is ApiState.loading -> {
                        Log.i("TAG", "LOOOOODING Fav: ")
                    }
                    is ApiState.SucessedWeather -> {
                        allFavViewModel.insertProducts(result.data)
                    }
                    else -> {
                    }
                }
            }
        }
        refreshFavorites()
    }
}
