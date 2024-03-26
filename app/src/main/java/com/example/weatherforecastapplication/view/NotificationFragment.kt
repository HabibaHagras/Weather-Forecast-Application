package com.example.weatherforecastapplication.view

import WeatherLocalDataSourceImp
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherforecastapplication.R

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.util.Log

import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.example.weatherforecastapplication.MainActivity
import com.example.weatherforecastapplication.databinding.FragmentNotificationBinding
import com.example.weatherforecastapplication.model2.RepositoryImp
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
import com.example.weatherforecastapplication.model2.WeatherData
import com.example.weatherforecastapplication.network.RemoteDataSourceImp
import com.example.weatherforecastapplication.view_model.notification
import com.example.weatherforecastapplication.view_model.notificationFactory
import java.util.*
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NotificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationFragment : Fragment() {

    companion object {
        const val CHANNEL_ID = "channelID"
        const val NOTIF_ID = 0
        const val PERMISSION_REQUEST_CODE = 123
        lateinit var allProductFactroy: notificationFactory
        lateinit var allProductViewModel: notification
    }

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
        val networkAvailability = NetworkAvailability()
        val isNetworkAvailable = networkAvailability.isNetworkAvailable(requireContext())
        if (isNetworkAvailable) {
            binding.ConstraintLayout.visibility = View.GONE
            createNotificationChannel()


            // Now you can use title and temperature to display or update your notification
            binding.btnShowNotif.setOnClickListener {
                var title = "Sample Title"
                var text = "This is a sample body notification"
                val selectedTime = getSelectedDateTime()

                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions()
                } else {
                    Log.i("TAG", "onViewCreated:scheduleNotification ")

                    scheduleNotification(title, text, selectedTime)
                }
            }
        }else{
            binding.ConstraintLayout.visibility = View.VISIBLE
        }
    }

    private fun getSelectedDateTime(): Calendar {
        val calendar = Calendar.getInstance()

        val datePicker = binding.datePicker
        calendar.set(datePicker.year, datePicker.month, datePicker.dayOfMonth)

        val timePicker = binding.timePicker
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
        calendar.set(Calendar.MINUTE, timePicker.minute)

        // Get seconds from the NumberPicker
        val secondsPicker = binding.secondsPicker
        val seconds = secondsPicker?.value ?: 0
        calendar.set(Calendar.SECOND, seconds)

        return calendar
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "channelName",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.BLUE
                enableLights(true)
            }
            val manager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun scheduleNotification(title: String, text: String, time: Calendar) {
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        intent.putExtra("title", title)
        intent.putExtra("text", text)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            NOTIF_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager =
            requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    time.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            // Handle SecurityException, e.g., request necessary permissions
            e.printStackTrace()
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        // Request necessary permissions here
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.SET_ALARM),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        var title = intent.getStringExtra("title")
        var text = intent.getStringExtra("text")
        Log.i("TAG", "onReceive: onReceive onReceive onReceive onReceive ")
        if (title != null && text != null) {
//            val notification = buildNotification(context,title,text)
//            Log.i("TAG", "onReceive: $notification")
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(intent)
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            }

            val allProductFactory = notificationFactory(
                RepositoryImp.getInstance(
                    RemoteDataSourceImp.getInstance(), WeatherLocalDataSourceImp(context)
                ),
                SharedPreferencesManager.getInstance(context)
            )

            val allProductViewModel = ViewModelProvider(
                ViewModelStore(),
                allProductFactory
            ).get(com.example.weatherforecastapplication.view_model.notification::class.java)
            allProductViewModel.getAllProducts()
            val notificationBuilder = NotificationCompat.Builder(context, NotificationFragment.CHANNEL_ID)
                .setSmallIcon(R.drawable.cloud_white_24dp)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)

            allProductViewModel.products.observeForever { value ->
                Log.i("TAG", "Observer: Observer")
                Log.i("TAG", "Observer: $value")

                val updatedTitle = value.name
                val updatedText = value.main.temp.toString()

                // Update notification content based on LiveData
                notificationBuilder.setContentTitle(updatedTitle)
                notificationBuilder.setContentText(updatedText)

                // Notify system to update the notification
                val notificationManager = NotificationManagerCompat.from(context)
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                }
                notificationManager.notify(0, notificationBuilder.build())
            }
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(0, notificationBuilder.build())


//           val notificationManager = NotificationManagerCompat.from(context)
//
//            if (ActivityCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.SET_ALARM
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                // TODO: Handle the case where permission is not granted.
//                // You can show a notification indicating the lack of permissions or request it.
//                return
//            }
//
//            notificationManager.notify(0, notification.build())
//        }
    }}

    private fun buildNotification(context: Context,title: String,text: String): NotificationCompat.Builder {

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }

        val allProductFactory = notificationFactory(
            RepositoryImp.getInstance(
                RemoteDataSourceImp.getInstance(), WeatherLocalDataSourceImp(context)
            ),
            SharedPreferencesManager.getInstance(context)
        )

        val allProductViewModel = ViewModelProvider(
            ViewModelStore(),
            allProductFactory
        ).get(notification::class.java)
        allProductViewModel.getAllProducts()
        val notificationBuilder = NotificationCompat.Builder(context, NotificationFragment.CHANNEL_ID)
            .setSmallIcon(R.drawable.cloud_white_24dp)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        allProductViewModel.products.observeForever { value ->
            Log.i("TAG", "Observer: Observer")
            Log.i("TAG", "Observer: $value")

            val updatedTitle = value.name
            val updatedText = value.main.temp.toString()

            // Update notification content based on LiveData
            notificationBuilder.setContentTitle(updatedTitle)
            notificationBuilder.setContentText(updatedText)

            // Notify system to update the notification
            val notificationManager = NotificationManagerCompat.from(context)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            notificationManager.notify(0, notificationBuilder.build())
        }

        return notificationBuilder
    }


}