package com.example.weatherforecastapplication.view.notifications_view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat

class DismissAlarmSoundReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Stop the alarm sound
        AlarmSoundReceiver.alarmMediaPlayer?.stop()
        AlarmSoundReceiver.alarmMediaPlayer?.release()
        AlarmSoundReceiver.alarmMediaPlayer = null

        // Cancel the notification
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(resultCode) // Make sure to use the same notification ID used for displaying the alarm notification
    }
}