package com.example.weatherforecastapplication.view

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import com.example.weatherforecastapplication.MainActivity2
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentSettingsBinding
import com.example.weatherforecastapplication.model.SharedPreferencesManager
import java.util.Locale


class SettingsFragment : Fragment() {
    private var isLanguageChanging = false

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
        binding.map.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
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