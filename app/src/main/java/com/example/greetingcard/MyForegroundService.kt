package com.example.greetingcard

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MyForegroundService : Service() {

    private val CHANNEL_ID = "ForegroundServiceChannel"
    private val NOTIFICATION_ID = 1
    private val binder = LocalBinder()

    // Inner class providing access to this service instance for binding
    inner class LocalBinder : Binder() {
        fun getService(): MyForegroundService = this@MyForegroundService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        // Build the notification required for a Foreground Service
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("My Foreground Service")
            .setContentText("The service has started")
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Standard system icon
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Start the service in the foreground immediately
        startForeground(NOTIFICATION_ID, notification)

        return START_STICKY
    }

    // Method to be called by your MainActivity when bound
    fun getMyGrade(): String {
        return "A+"
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }
}
