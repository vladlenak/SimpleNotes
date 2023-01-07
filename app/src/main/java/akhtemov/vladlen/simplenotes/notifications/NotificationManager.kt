package akhtemov.vladlen.simplenotes.notifications

import akhtemov.vladlen.simplenotes.utility.NotificationHelper
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.octopus.inc.domain.models.NoteModel
import java.util.*

object NotificationManager {

    fun start(context: Context, noteList: List<NoteModel>) {
        var i = 1
        for (note in noteList) {
            if (note.time.isNotEmpty() && note.date.isNotEmpty()) {
                stopReminder(
                    context = context,
                    reminderId = i
                )
                startReminder(
                    context = context,
                    reminderTitle = note.title,
                    reminderDesc = note.desc,
                    reminderDate = note.date,
                    reminderTime = note.time,
                    reminderId = i
                )
                i++
            }
        }
    }

    private fun startReminder(
        context: Context,
        reminderTitle: String,
        reminderDesc: String,
        reminderDate: String,
        reminderTime: String,
        reminderId: Int
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val (hours, min) = reminderTime.split(":").map { it.toInt() }
        val (year, month, day) = reminderDate.split("-").map { it.toInt() }
        val intent =
            Intent(
                context.applicationContext,
                NotificationBroadcastReceiver::class.java
            ).let { intent ->
                intent.putExtra(NotificationBroadcastReceiver.ID_EXTRA_ID, reminderId)
                intent.putExtra(NotificationBroadcastReceiver.TITLE_EXTRA_ID, reminderTitle)
                intent.putExtra(NotificationBroadcastReceiver.DESC_EXTRA_ID, reminderDesc)

                PendingIntent.getBroadcast(
                    context.applicationContext,
                    reminderId,
                    intent,
                    NotificationHelper.getCorrectlyFlag()
                )
            }
        val calendar: Calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, min)
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, day)
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

    private fun stopReminder(
        context: Context,
        reminderId: Int
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationBroadcastReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context.applicationContext,
                reminderId,
                intent,
                NotificationHelper.getCorrectlyFlag()
            )
        }
        alarmManager.cancel(intent)
    }
}