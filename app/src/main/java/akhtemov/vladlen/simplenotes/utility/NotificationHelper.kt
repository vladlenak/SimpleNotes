package akhtemov.vladlen.simplenotes.utility

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.presentation.MainActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class NotificationHelper {
    companion object {
        private const val DEFAULT_CHANEL_ID = "default_chanel_id"
        private const val DEFAULT_CHANEL_NAME = "default_chanel_name"
        private const val DEFAULT_NOTIFICATION_ID = 101

        fun createNotificationsChannels(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    DEFAULT_CHANEL_ID, DEFAULT_CHANEL_NAME, NotificationManager.IMPORTANCE_HIGH
                )
                ContextCompat.getSystemService(context, NotificationManager::class.java)
                    ?.createNotificationChannel(channel)
            }
        }

        fun getCorrectlyFlag(): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        }

        fun showNotification(context: Context, title: String, desc: String) {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val builder = NotificationCompat.Builder(context, DEFAULT_CHANEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(desc)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(DEFAULT_NOTIFICATION_ID, builder.build())
            }
        }
    }
}