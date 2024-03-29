package com.example.weatherforecastapplication.view.notifications_view
import WeatherLocalDataSourceImp
import android.Manifest
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
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
import kotlinx.coroutines.launch

class NotificationsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(intent)
                getPendingIntent(resultCode, PendingIntent.FLAG_IMMUTABLE)
            }
            val notificationBuilder = NotificationCompat.Builder(context,
                NotificationFragment.CHANNEL_ID
            )
                .setSmallIcon(R.drawable.cloud_white_24dp)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
            val repo = RepositoryImp(RemoteDataSourceImp.getInstance(),WeatherLocalDataSourceImp(context))
            CoroutineScope(Dispatchers.IO).launch {
                repo.getWeatherWithCity(
                    SharedPreferencesManager.getInstance(context).getGpsLat().toDouble()
                    ,
                    SharedPreferencesManager.getInstance(context).getGpsLon().toDouble(),"7f6473d2786753ccda5811e204914fff"
                    ,
                    SharedPreferencesManager.getInstance(context).getUnits().toString(),
                    SharedPreferencesManager.getInstance(context).getLanguageUnit().toString()).collect {
                    val updatedTitle = it.name
                    val updatedText = it.main.temp.toString()
                    notificationBuilder.setContentTitle(updatedTitle)
                    notificationBuilder.setContentText(updatedText)
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
                    notificationManager.notify( resultCode, notificationBuilder.build())
                }
            }
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(resultCode, notificationBuilder.build())

    }

}