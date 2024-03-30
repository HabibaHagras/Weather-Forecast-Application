package com.example.weatherforecastapplication.view.notifications_view

import WeatherLocalDataSourceImp
import android.Manifest
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
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
import com.example.weatherforecastapplication.view.NetworkAvailability
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
    var NotificationOnly:Boolean=false

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
        val networkAvailability = NetworkAvailability()
        val isNetworkAvailable = networkAvailability.isNetworkAvailable(requireContext())
        if (isNetworkAvailable) {
            binding.ConstraintLayout.visibility = View.GONE
        requestPermissions()
        factory = AlarmSoundFactory(
            RepositoryImp.getInstance(
                RemoteDataSourceImp.getInstance(),
                WeatherLocalDataSourceImp(requireContext())
            ), SharedPreferencesManager.getInstance(requireContext())
        )
        viewModel= ViewModelProvider(this, factory).get(AlarmSound::class.java)
        binding.floatingActionButton.setOnClickListener {
            requestPermissions()
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_alarm_type, null)
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
                .setView(dialogView)

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
            dialogView.findViewById<Button>(R.id.btnAlarmWithNotification)?.setOnClickListener {
                alertDialog.dismiss()
                showDateTimePicker(false)
            }
            dialogView.findViewById<Button>(R.id.btnNotificationOnly)?.setOnClickListener {
                alertDialog.dismiss()
                NotificationOnly=true
                showDateTimePicker(true)            }
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
            else{
                binding.ConstraintLayout.visibility = View.VISIBLE
            }


    }
    private fun showDateTimePicker(NotificationOnly:Boolean) {
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
                        scheduleAlarm(lat,log,selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute,requestCode,NotificationOnly)

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
    private fun scheduleAlarm(lat:Double,log:Double,year: Int, month: Int, day: Int, hour: Int, minute: Int, requestCode: Int,NotificationOnly:Boolean) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        if (checkNotificationPermissions()) {
            val intent = Intent(requireContext(), AlarmSoundReceiver::class.java)
            intent.putExtra("latitude", lat)
            intent.putExtra("longitude", log)
            intent.putExtra("NotificationOnly", NotificationOnly)
//        val intentNotification = Intent(requireContext(), NotificationsReceiver::class.java)
//        intentNotification.putExtra("latitude", lat)
//        intentNotification.putExtra("longitude", log)
            val pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
//        val pendingIntentNotication = PendingIntent.getBroadcast(
//            requireContext(),
//            requestCode,
//            intentNotification,
//            PendingIntent.FLAG_IMMUTABLE
//        )
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                NotificationFragment.CHANNEL_ID,
//                "channelName",
//                NotificationManager.IMPORTANCE_DEFAULT
//            ).apply {
//                lightColor = Color.BLUE
//                enableLights(true)
//            }
//            val manager =
//                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            manager.createNotificationChannel(channel)
//        }
            val alarmManager =
                requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
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
//            alarmManager.setExact(
//                AlarmManager.RTC_WAKEUP,
//                calendar.timeInMillis,
//                pendingIntentNotication
//            )
            } catch (e: SecurityException) {
                AlarmFragment.openDrawOverOtherAppsSettings(requireContext())
//            requestPermissions()

            }
        } else {
            requestPermissions()
            }
        }
    private fun checkNotificationPermissions(): Boolean {
        // Check if notification permissions are granted
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.SET_ALARM
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestPermissions() {
        // Request necessary permissions here
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.SET_ALARM),
            NotificationFragment.PERMISSION_REQUEST_CODE
        )
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