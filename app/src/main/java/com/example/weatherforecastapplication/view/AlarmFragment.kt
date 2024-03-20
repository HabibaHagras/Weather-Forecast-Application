package com.example.weatherforecastapplication.view
import WeatherLocalDataSourceImp
import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapplication.MainActivity
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentAlarmBinding
import com.example.weatherforecastapplication.model.RepositoryImp
import com.example.weatherforecastapplication.model2.WeatherData
import com.example.weatherforecastapplication.network.RemoteDataSourceImp
import com.example.weatherforecastapplication.view_model.notification
import com.example.weatherforecastapplication.view_model.notificationFactory
import java.util.Calendar




class AlarmFragment : Fragment() {
    private lateinit var binding: FragmentAlarmBinding
    private lateinit var alarmManager: AlarmManager
    lateinit var allProductFactroy: notificationFactory
    lateinit var allProductViewModel: notification
     var title: String=""
     var text: String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        binding.setAlarmButton.setOnClickListener {
            setAlarm()
        }
      allProductFactroy = notificationFactory(
            RepositoryImp.getInstance(
                RemoteDataSourceImp.getInstance(),WeatherLocalDataSourceImp(requireContext())))
      allProductViewModel = ViewModelProvider(this,
            allProductFactroy
        ).get(notification::class.java)

        allProductViewModel.products.observe(viewLifecycleOwner,

            object: Observer<WeatherData> {
                override fun onChanged(value: WeatherData) {
                    Log.i("TAG", "Observer: Observer")
                    if (value != null) {
                        Log.i("TAG", "Observer: $value")

                        title = value.name
                        text = value.main.temp.toString()
                    }
                }})



    }
    private fun setAlarm() {
        val hour = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            binding.timePicker.hour
        } else {
            binding.timePicker.currentHour
        }
        val minute = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            binding.timePicker.minute
        } else {
            binding.timePicker.currentMinute
        }

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        intent.putExtra("title", title)
        intent.putExtra("temp", text)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        !alarmManager.canScheduleExactAlarms()
                    } else {
                        TODO("VERSION.SDK_INT < S")
                    }
                ) {
                    // Handle the case where exact alarms cannot be scheduled
                    // You can use an alternative method for setting alarms here
                    return
                }
            }

            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } catch (e: SecurityException) {
            // Handle SecurityException here
            openDrawOverOtherAppsSettings(requireContext())

            // You can show a message to the user indicating that the app doesn't have permission to set exact alarms
        }
    }

//     fun openDrawOverOtherAppsSettings(context: Context) {
//        Log.i("TAG", "openDrawOverOtherAppsSettings: ")
//        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + requireContext().packageName))
//         context.startActivity(intent)
//    }
    companion object {
        @JvmStatic
        fun openDrawOverOtherAppsSettings(context: Context) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.packageName))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK // Add the flag here
            context.startActivity(intent)
        }
        fun newInstance(param1: String, param2: String) =
            AlarmFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

}

//
//class AlarmReceiver : BroadcastReceiver() {
//    companion object {
//        var alarmMediaPlayer: MediaPlayer? = null
//    }
//
//    override fun onReceive(context: Context, intent: Intent) {
//        val notificationIntent = Intent(context, MainActivity::class.java)
//        val title = intent.getStringExtra("title")
//        val text = intent.getStringExtra("temp")
//        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        val pendingIntent = PendingIntent.getActivity(
//            context,
//            0,
//            notificationIntent,
//            PendingIntent.FLAG_IMMUTABLE // Add mutability flag here
//        )
//        val dismissIntent = Intent(context, DismissAlarmReceiver::class.java)
//        val dismissPendingIntent = PendingIntent.getBroadcast(
//            context,
//            0,
//            dismissIntent,
//            PendingIntent.FLAG_IMMUTABLE // Add mutability flag here
//        )
//
//// Create the action for the dismiss button
//        val dismissAction = NotificationCompat.Action.Builder(
//            R.drawable.air_white_24dp,
//            "Dismiss",
//            dismissPendingIntent
//        ).build()
//
//
//        // Create a notification
//        val builder = NotificationCompat.Builder(context, "default")
//            .setSmallIcon(R.drawable.alarm_black_24dp)
//            .setContentTitle(title)
//            .setContentText(text)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//            .addAction(dismissAction)
//
//
//        // Show the notification
//        val notificationManager = NotificationManagerCompat.from(context)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                "default",
//                "Alarm Channel",
//                NotificationManager.IMPORTANCE_HIGH
//            )
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//        notificationManager.notify(0, builder.build())
////        val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
////        val ringtonePlayer = RingtoneManager.getRingtone(context, ringtone)
////        ringtonePlayer.play()
//        val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
//        alarmMediaPlayer = MediaPlayer.create(context, ringtone)
//        alarmMediaPlayer?.isLooping = true
//        alarmMediaPlayer?.start()
//
//    }
//}
class AlarmReceiver : BroadcastReceiver() {
    companion object {
        var alarmMediaPlayer: MediaPlayer? = null
        private  val LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        // Fetch title and text from the intent
        val title = intent.getStringExtra("title")
        val text = intent.getStringExtra("temp")

        // Create a notification intent
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE // Add mutability flag here
        )

        // Create a dismiss intent
        val dismissIntent = Intent(context, DismissAlarmReceiver::class.java)
        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            dismissIntent,
            PendingIntent.FLAG_IMMUTABLE // Add mutability flag here
        )

        // Create the action for the dismiss button
        val dismissAction = NotificationCompat.Action.Builder(
            R.drawable.air_white_24dp,
            "Dismiss",
            dismissPendingIntent
        ).build()

        // Create a notification builder
        val builder = NotificationCompat.Builder(context, "default")
            .setSmallIcon(R.drawable.alarm_black_24dp)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(dismissAction)

        // Show the notification
        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default",
                "Alarm Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(0, builder.build())

        // Play the alarm sound
        val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        alarmMediaPlayer = MediaPlayer.create(context, ringtone)
        alarmMediaPlayer?.isLooping = true
        alarmMediaPlayer?.start()


        val alertView = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null)

        // Add the alert dialog view to the window manager
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        layoutParams.gravity = Gravity.TOP
        windowManager.addView(alertView, layoutParams)
        var cityname:TextView=alertView.findViewById<Button>(R.id.titleTextView)
        cityname.text=title
        var temp:TextView=alertView.findViewById<Button>(R.id.messageTextView)
        temp.text=text

        val dismissButton = alertView.findViewById<Button>(R.id.dismissButton)
        dismissButton.setOnClickListener {
            // Stop the alarm sound
            alarmMediaPlayer?.stop()
            alarmMediaPlayer?.release()
            alarmMediaPlayer = null

            // Remove the alert dialog view from the window manager
            windowManager.removeView(alertView)

            // Cancel the notification
            notificationManager.cancel(0) // Make sure to use the same notification ID used for displaying the alarm notification
        }



        /*

        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        layoutParams.gravity = Gravity.TOP

        val alertView = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null)
//        val windowManager = context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        windowManager.addView(alertView, layoutParams)
        val windowManager = context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
            // Permission not granted, handle accordingly
            // You can prompt the user to grant the permission here
            // For demonstration purposes, I'm simply logging an error

            AlarmFragment.openDrawOverOtherAppsSettings(context)

            Log.i("AlarmReceiver", "SYSTEM_ALERT_WINDOW permission not granted")

        } else {
            // Permission granted, add the view to the window manager
            windowManager.addView(alertView, layoutParams)
        }
        */
    }
}

class DismissAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Stop the alarm sound
        AlarmReceiver.alarmMediaPlayer?.stop()
        AlarmReceiver.alarmMediaPlayer?.release()
        AlarmReceiver.alarmMediaPlayer = null

        // Cancel the notification
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(0) // Make sure to use the same notification ID used for displaying the alarm notification
    }
}



