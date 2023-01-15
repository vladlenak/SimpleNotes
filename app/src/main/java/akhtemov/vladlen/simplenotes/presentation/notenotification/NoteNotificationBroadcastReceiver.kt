package akhtemov.vladlen.simplenotes.presentation.notenotification

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.presentation.main.MainActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class NoteNotificationBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID_EXTRA_ID = "notification_id_extra_id"
        const val NOTIFICATION_TITLE_EXTRA_ID = "notification_title_extra_id"
        const val NOTIFICATION_DESC_EXTRA_ID = "notification_desc_extra_id"

        const val DEFAULT_NOTIFICATION_ID = 1
        const val DEFAULT_CHANNEL_ID = "default_channel_id"
        private const val DEFAULT_CHANNEL_NAME = "default_channel_name"

        fun createNotificationsChannels(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
                )
                ContextCompat
                    .getSystemService(context, NotificationManager::class.java)
                    ?.createNotificationChannel(channel)
            }
        }

        fun getCorrectlyFlag(): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else PendingIntent.FLAG_UPDATE_CURRENT
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra(NOTIFICATION_ID_EXTRA_ID, DEFAULT_NOTIFICATION_ID)
        val notificationTitle = intent.getStringExtra(NOTIFICATION_TITLE_EXTRA_ID)
        val notificationDesc = intent.getStringExtra(NOTIFICATION_DESC_EXTRA_ID)

        val notificationManager = ContextCompat
            .getSystemService(context, NotificationManager::class.java) as NotificationManager

        notificationTitle?.let {
            notificationManager.sendReminderNotification(
                context = context,
                notificationId = notificationId,
                notificationTitle = it,
                notificationDesc = notificationDesc ?: ""
            )
        }
    }

    private fun NotificationManager.sendReminderNotification(
        context: Context,
        notificationId: Int,
        notificationTitle: String,
        notificationDesc: String
    ) {
        val intent = Intent(context, MainActivity::class.java).let {
            PendingIntent.getActivity(context, 0, it, getCorrectlyFlag())
        }
        val builder = NotificationCompat
            .Builder(context, DEFAULT_CHANNEL_ID)
            .setContentTitle(notificationTitle)
            .setContentText(notificationDesc)
            .setSmallIcon(R.drawable.event_note)
            .setStyle(NotificationCompat.BigTextStyle().bigText(notificationDesc))
            .setContentIntent(intent)
            .setAutoCancel(true)

        notify(notificationId, builder.build())
    }
}