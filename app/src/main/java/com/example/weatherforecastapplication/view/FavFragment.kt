package com.example.weatherforecastapplication.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.HomeFragment
import com.example.weatherforecastapplication.MainActivity
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.model.Repository
import com.google.android.material.floatingactionbutton.FloatingActionButton

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

    private lateinit var adapter:FavAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fav, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
         adapter = FavAdapter(FavFragment(),this)
        recyclerView.adapter = adapter
        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            showAddCityDialog()

//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, NotificationFragment())
//                .addToBackStack(null)
//                .commit()
        }

        return view
    }

    private fun showAddCityDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.fav_city_dialog, null)
        val editTextFavCity = dialogView.findViewById<EditText>(R.id.edit_text_fav_city)
        val saveButton = dialogView.findViewById<Button>(R.id.button_save)

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Add Favorite City")
        val alertDialog = dialogBuilder.create()

        saveButton.setOnClickListener {
            val city = editTextFavCity.text.toString().trim()
            if (city.isNotEmpty()) {
                adapter.addCity(city)
                alertDialog.dismiss()
            } else {
                // Handle empty city name
                editTextFavCity.error = "Please enter a city name"
            }
        }

        alertDialog.show()
    }

    override fun OnCLickIteamFav(city: String) {
        val anotherFragment = HomeFragment()

        // Pass the city name to the target fragment using arguments
        val bundle = Bundle().apply {
            putString("selected_city", city)
        }
        anotherFragment.arguments = bundle

        // Replace the current fragment with the target fragment
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, anotherFragment)
            .addToBackStack(null)
            .commit()
//        // Create an Intent to start the MainActivity
//        val intent = Intent(requireContext(), MainActivity::class.java)
//        // Pass any data you want to the MainActivity using Intent extras
//        intent.putExtra("selected_city", city)
//        // Start the MainActivity
//        startActivity(intent)
    }
}