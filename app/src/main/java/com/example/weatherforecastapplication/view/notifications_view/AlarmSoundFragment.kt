package com.example.weatherforecastapplication.view.notifications_view

import WeatherLocalDataSourceImp
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentAlarmSoundBinding
import com.example.weatherforecastapplication.databinding.FragmentSettingsBinding
import com.example.weatherforecastapplication.model2.Alarm
import com.example.weatherforecastapplication.model2.RepositoryImp
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
import com.example.weatherforecastapplication.network.ApiState
import com.example.weatherforecastapplication.network.RemoteDataSourceImp
import com.example.weatherforecastapplication.view_model.AlarmSound
import com.example.weatherforecastapplication.view_model.AlarmSoundFactory
import com.example.weatherforecastapplication.view_model.Fav
import com.example.weatherforecastapplication.view_model.FavFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

class AlarmSoundFragment : Fragment(),AlarmListener {
    lateinit var binding: FragmentAlarmSoundBinding
    lateinit var viewModel: AlarmSound
    lateinit var factory:AlarmSoundFactory
    private lateinit var alarmAdapter: AlarmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmSoundBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = AlarmSoundFactory(
            RepositoryImp.getInstance(
                RemoteDataSourceImp.getInstance(),
                WeatherLocalDataSourceImp(requireContext())
            ), SharedPreferencesManager.getInstance(requireContext())
        )
        viewModel= ViewModelProvider(this, factory).get(AlarmSound::class.java)
        binding.floatingActionButton.setOnClickListener {
            showDateTimePicker()
        }
        alarmAdapter = AlarmAdapter(AlarmFragment()
        ,this
        )

        binding.rvAlrams.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = alarmAdapter
        }
        lifecycleScope.launch {
            viewModel.weatherStateFlow.collectLatest { result ->
                when (result) {
                    is ApiState.loading -> {
                        //    progressBar.visibility = ProgressBar.VISIBLE
                        Log.i("TAG", "LOOOOODING Fav: ")

                    }

                    is ApiState.SucessedAlarm -> {
                        //     progressBar.visibility = ProgressBar.GONE
                        alarmAdapter.setData(result.data)
                    }

                    else -> {
                        //   progressBar.visibility = ProgressBar.GONE

                        Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT)

                    }


                }
            }
        }
    }
    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    { _, selectedHour, selectedMinute ->
                        val requestCode = generateUniqueRequestCode()
                        var lat = SharedPreferencesManager.getInstance(requireContext()).getGpsLat().toDouble()
                        var log = SharedPreferencesManager.getInstance(requireContext()).getGpsLon().toDouble()
                        viewModel.insertAlarm(lat,log,selectedYear, selectedMonth,selectedDay,selectedHour,selectedMinute,requestCode)
                        scheduleAlarm(lat,log,selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute,requestCode)

                        Toast.makeText(
                            requireContext(),
                            "Selected Date: $selectedDay/${selectedMonth + 1}/$selectedYear " +
                                    "and Time: $selectedHour:$selectedMinute",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    hour,
                    minute,
                    true
                )
                timePickerDialog.show()
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
    private fun generateUniqueRequestCode(): Int {
        return System.currentTimeMillis().toInt()
    }
    private fun scheduleAlarm(lat:Double,log:Double,year: Int, month: Int, day: Int, hour: Int, minute: Int, requestCode: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        val intent = Intent(requireContext(), AlarmSoundReceiver::class.java)
        intent.putExtra("latitude", lat)
        intent.putExtra("longitude", log)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        !alarmManager.canScheduleExactAlarms()
                    } else {
                        TODO("VERSION.SDK_INT < S")
                    }
                ) {
                    return
                }
            }
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } catch (e: SecurityException) {
            AlarmFragment.openDrawOverOtherAppsSettings(requireContext())
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AlarmSoundFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun deleteAlarm(alarm: Alarm) {
        viewModel.delete(alarm)

    }
}