package com.example.weatherforecastapplication.view.notifications_view
import WeatherLocalDataSourceImp
import android.Manifest
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
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.example.weatherforecastapplication.MainActivity
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.model2.RepositoryImp
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
import com.example.weatherforecastapplication.network.RemoteDataSourceImp
import com.example.weatherforecastapplication.view_model.notification
import com.example.weatherforecastapplication.view_model.notificationFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlarmSoundReceiver : BroadcastReceiver() {
    companion object {
        var alarmMediaPlayer: MediaPlayer? = null
        private  val LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
    }
    override fun onReceive(context: Context, intent: Intent) {
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val NotificationOnly=intent.getBooleanExtra("NotificationOnly",false)
        if (Settings.canDrawOverlays(context)) {
            val notificationIntent = Intent(context, MainActivity::class.java)
            notificationIntent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(
                context,
                resultCode,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE // Add mutability flag here
            )
            Log.i("TAG,", "onReceive:2 ")
            // Create a dismiss intent
            val dismissIntent = Intent(context, DismissAlarmSoundReceiver::class.java)
            val dismissPendingIntent = PendingIntent.getBroadcast(
                context,
                resultCode,
                dismissIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

            val dismissAction = NotificationCompat.Action.Builder(
                R.drawable.air_white_24dp,
                "Dismiss",
                dismissPendingIntent
            ).build()
            // Play the alarm sound
            val repo =
                RepositoryImp(RemoteDataSourceImp.getInstance(), WeatherLocalDataSourceImp(context))
            CoroutineScope(Dispatchers.IO).launch {
                repo.getWeatherWithCity(
                    latitude,
                    longitude, "7f6473d2786753ccda5811e204914fff",
                    SharedPreferencesManager.getInstance(context).getUnits(),
                    SharedPreferencesManager.getInstance(context).getLanguageUnit().toString()
                ).collectLatest {
                    Log.i("TAGn", "onReceive: ${SharedPreferencesManager.getInstance(context).getUnits()} ")
                    // Create a notification builder
                    val builder = NotificationCompat.Builder(context, "default")
                        .setSmallIcon(R.drawable.cloud_white_24dp)
                        .setContentTitle(it.name)
                        .setContentText(it.main.temp.toString())
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .addAction(dismissAction)
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

                    }
                    notificationManager.notify(resultCode, builder.build())
                    if(!NotificationOnly){
                        val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                        alarmMediaPlayer = MediaPlayer.create(context, ringtone)
                        alarmMediaPlayer?.isLooping = true
                        alarmMediaPlayer?.start()
                    Handler(Looper.getMainLooper()).post {
                        val alertView =
                            LayoutInflater.from(context).inflate(R.layout.alert_dialog, null)

                        // Add the alert dialog view to the window manager
                        val windowManager =
                            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
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
                        var cityname: TextView =
                            alertView.findViewById<TextView>(R.id.titleTextView)
                        cityname.text = it.name
                        var temp: TextView = alertView.findViewById<TextView>(R.id.messageTextView)
                        temp.text = it.main.temp.toString()
                        var desc: TextView = alertView.findViewById<TextView>(R.id.desc)
                        desc.text = it.weather[0].description
                        val dismissButton = alertView.findViewById<Button>(R.id.dismissButton)
                        dismissButton.setOnClickListener {
                            // Stop the alarm sound
                            alarmMediaPlayer?.stop()
                            alarmMediaPlayer?.release()
                            alarmMediaPlayer = null
                            // Remove the alert dialog view from the window manager
                            windowManager.removeView(alertView)

                            // Cancel the notification
                            notificationManager.cancel(resultCode) // Make sure to use the same notification ID used for displaying the alarm notification
                        }

                    }


                }}
            }
        } else {
            openDrawOverOtherAppsSettings(context)
        }


    }


        private fun openDrawOverOtherAppsSettings(context: Context) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }




