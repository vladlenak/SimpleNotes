package akhtemov.vladlen.simplenotes.presentation.notifications

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.presentation.MainActivity
import akhtemov.vladlen.simplenotes.utility.NotificationHelper
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat


class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val TITLE_EXTRA_ID = "title_id"
        const val DESC_EXTRA_ID = "desc_id"
        const val ID_EXTRA_ID = "id_id"

        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "chanel_id"
    }

    /**
     * sends notification when receives alarm
     * and then reschedule the reminder again
     * */
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        val id = intent.getStringExtra(ID_EXTRA_ID)
        val title = intent.getStringExtra(TITLE_EXTRA_ID)
        val desc = intent.getStringExtra(DESC_EXTRA_ID)

        if (title != null) {
            if (desc != null) {
                notificationManager.sendReminderNotification(
                    applicationContext = context,
                    channelId = CHANNEL_ID,
                    notificationId = id?.toInt(),
                    title = title,
                    desc = desc
                )
            } else {
                notificationManager.sendReminderNotification(
                    applicationContext = context,
                    channelId = CHANNEL_ID,
                    notificationId = id?.toInt(),
                    title = title,
                    desc = ""
                )
            }
        }

        // Remove this line if you don't want to reschedule the reminder
//        RemindersManager.startReminder(context.applicationContext)
    }

    // TODO перенести код в NotificationHelper
    private fun NotificationManager.sendReminderNotification(
        applicationContext: Context,
        channelId: String,
        notificationId: Int?,
        title: String,
        desc: String
    ) {
        val contentIntent = Intent(applicationContext, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            1,
            contentIntent,
            NotificationHelper.getCorrectlyFlag()
        )
        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(desc)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(desc)
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notify(notificationId ?: NOTIFICATION_ID, builder.build())
    }
}




