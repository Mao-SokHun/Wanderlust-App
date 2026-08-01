package com.example.wanderlust.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.wanderlust.R

/**
 * Wanderlust Notification Helper.
 *
 * When Firebase Messaging is fully configured (google-services.json added),
 * create WanderlustFirebaseMessagingService extending FirebaseMessagingService and
 * call [showNotification] from onMessageReceived.
 *
 * For now, this helper can be called from within the app itself for
 * local in-app notifications (e.g., simulating FCM receipt while polling).
 */
object NotificationHelper {

    const val CHANNEL_CHAT_ID    = "wl_chat"
    const val CHANNEL_PROMO_ID   = "wl_promo"
    const val CHANNEL_ALERT_ID   = "wl_alert"

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannels(
            listOf(
                NotificationChannel(
                    CHANNEL_CHAT_ID,
                    "Chat Messages",
                    NotificationManager.IMPORTANCE_HIGH
                ).also { it.description = "Notifications for new direct messages" },
                NotificationChannel(
                    CHANNEL_PROMO_ID,
                    "Promotions & Offers",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).also { it.description = "Special deals and tour promotions" },
                NotificationChannel(
                    CHANNEL_ALERT_ID,
                    "System Alerts",
                    NotificationManager.IMPORTANCE_HIGH
                ).also { it.description = "Admin broadcasts and important alerts" },
            )
        )
    }

    fun showNotification(
        context: Context,
        title: String,
        body: String,
        channelId: String = CHANNEL_ALERT_ID,
        notificationId: Int = System.currentTimeMillis().toInt(),
        intent: Intent? = null,
    ) {
        val pendingIntent = intent?.let {
            PendingIntent.getActivity(
                context,
                0,
                it,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .apply { pendingIntent?.let { setContentIntent(it) } }
            .build()

        try {
            NotificationManagerCompat.from(context).notify(notificationId, notification)
        } catch (e: SecurityException) {
            // POST_NOTIFICATIONS permission not granted on Android 13+
        }
    }
}
