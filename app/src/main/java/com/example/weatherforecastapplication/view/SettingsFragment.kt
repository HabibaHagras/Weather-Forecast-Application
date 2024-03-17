package com.example.weatherforecastapplication.view

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startActivity(Intent(requireContext(), MapsActivity::class.java))
            }
        }
        binding.switchLanguageArabic.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setAppLanguage("ar")
                startActivity(Intent(requireContext(), MainActivity2::class.java))
            }
        }
        binding.switchLanguageEnglish.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setAppLanguage("en")
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