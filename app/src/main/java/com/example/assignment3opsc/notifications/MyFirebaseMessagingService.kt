package com.example.assignment3opsc.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.assignment3opsc.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val CHANNEL_ID = "cinematic_updates"
        private const val CHANNEL_NAME = "Cinematic Updates"
        private const val CHANNEL_DESC = "Real-time alerts and updates"
        private const val TAG = "FCM"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "FCM token: $token")
        // TODO: send this token to your backend if you target specific users/devices
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        createChannelIfNeeded()

        val title = message.notification?.title ?: "Cinematic"
        val body  = message.notification?.body ?: "You have a new notification"

        val notif = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // put a real icon in res/drawable/
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this).notify(System.currentTimeMillis().toInt(), notif)
    }

    private fun createChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(NotificationManager::class.java)
            if (nm.getNotificationChannel(CHANNEL_ID) == null) {
                val ch = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                ch.description = CHANNEL_DESC
                nm.createNotificationChannel(ch)
            }
        }
    }
}
