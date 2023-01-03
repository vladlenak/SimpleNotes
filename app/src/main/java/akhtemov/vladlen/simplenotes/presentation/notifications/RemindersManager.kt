package akhtemov.vladlen.simplenotes.presentation.notifications

import akhtemov.vladlen.simplenotes.utility.NotificationHelper
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.*

object RemindersManager {

    private const val REMINDER_NOTIFICATION_REQUEST_CODE = 123

    // TODO дубль EXTRA
    private const val TITLE_EXTRA_ID = "title_id"
    private const val DESC_EXTRA_ID = "desc_id"

    fun startReminder(
        context: Context,
        reminderTitle: String,
        reminderDesc: String,
        reminderTime: String,
        reminderId: Int = REMINDER_NOTIFICATION_REQUEST_CODE
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // TODO сохранить образец кода
        val (hours, min) = reminderTime.split(":").map { it.toInt() }

        val intent =
            Intent(context.applicationContext, AlarmReceiver::class.java).let { intent ->
                intent.putExtra(TITLE_EXTRA_ID, reminderTitle)
                intent.putExtra(DESC_EXTRA_ID, reminderDesc)

                PendingIntent.getBroadcast(
                    context.applicationContext,
                    reminderId,
                    intent,
                    NotificationHelper.getCorrectlyFlag()
                )
            }

        val calendar: Calendar = Calendar.getInstance(Locale.ENGLISH).apply {
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, min)
        }

        // If the trigger time you specify is in the past, the alarm triggers immediately.
        // if soo just add one day to required calendar
        // Note: i'm also adding one min cuz if the user clicked on the notification as soon as
        // he receive it it will reschedule the alarm to fire another notification immediately
        if (Calendar.getInstance(Locale.ENGLISH)
                .apply { add(Calendar.MINUTE, 1) }.timeInMillis - calendar.timeInMillis > 0
        ) {
            calendar.add(Calendar.DATE, 1)
        }

        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(calendar.timeInMillis, intent),
            intent
        )
    }

    fun stopReminder(
        context: Context,
        reminderId: Int = REMINDER_NOTIFICATION_REQUEST_CODE
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context,
                reminderId,
                intent,
                0
            )
        }
        alarmManager.cancel(intent)
    }
}