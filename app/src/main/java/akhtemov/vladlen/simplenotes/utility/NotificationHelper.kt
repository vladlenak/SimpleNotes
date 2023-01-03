package akhtemov.vladlen.simplenotes.utility

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.presentation.MainActivity
import akhtemov.vladlen.simplenotes.presentation.notelist.NoteListFragment
import android.app.Activity
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
        private const val TAG = "NotificationHelper"

        fun createNotificationsChannels(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // TODO вывести константы в отдельный класс так как используются в нескольких классах
                val channel = NotificationChannel(
                    NoteListFragment.CHANNEL_ID,
                    NoteListFragment.CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
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

            val builder = NotificationCompat.Builder(context, NoteListFragment.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(desc)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(NoteListFragment.NOTIFICATION_ID, builder.build())
            }
        }

        fun createNotificationChannel(activity: Activity) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = activity.getString(R.string.channel_name)
                val descriptionText = activity.getString(R.string.channel_description)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(NoteListFragment.CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}