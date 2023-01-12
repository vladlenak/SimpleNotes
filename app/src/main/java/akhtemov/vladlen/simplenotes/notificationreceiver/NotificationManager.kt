package akhtemov.vladlen.simplenotes.notificationreceiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.octopus.inc.domain.models.NoteModel
import java.util.*

object NotificationManager {

    fun restartReminders(context: Context, noteList: List<NoteModel>) {
        var i = 1
        noteList.forEach { note ->
            if (note.time.isNotEmpty() && note.date.isNotEmpty()) {
                stopReminder(context = context, reminderId = i)
                startReminder(context = context, reminderId = i, note = note)
                i++
            }
        }
    }

    private fun startReminder(context: Context, reminderId: Int, note: NoteModel) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val (hours, min) = note.time.split(":").map { it.toInt() }
        val (year, month, day) = note.date.split("-").map { it.toInt() }
        val calendar: Calendar = Calendar.getInstance(Locale.ENGLISH).apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, min)
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, day)
        }
        val intent = Intent(
            context.applicationContext,
            NotificationBroadcastReceiver::class.java
        ).apply {
            putExtra(NotificationBroadcastReceiver.NOTIFICATION_ID_EXTRA_ID, reminderId)
            putExtra(NotificationBroadcastReceiver.NOTIFICATION_TITLE_EXTRA_ID, note.title)
            putExtra(NotificationBroadcastReceiver.NOTIFICATION_DESC_EXTRA_ID, note.desc)
        }.let {
            PendingIntent.getBroadcast(
                context.applicationContext,
                reminderId,
                it,
                NotificationBroadcastReceiver.getCorrectlyFlag()
            )
        }

        // If the trigger time you specify is in the past, the alarm triggers immediately.
        // if soo just add one day to required calendar
        // Note: i'm also adding one min cuz if the user clicked on the notification as soon as
        // he receive it it will reschedule the alarm to fire another notification immediately
        if (Calendar.getInstance(Locale.ENGLISH)
                .apply { add(Calendar.MINUTE, 1) }
                .timeInMillis - calendar.timeInMillis > 0
        ) calendar.add(Calendar.DATE, 1)

        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(calendar.timeInMillis, intent),
            intent
        )
    }

    private fun stopReminder(context: Context, reminderId: Int) {
        val intent = Intent(context, NotificationBroadcastReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context.applicationContext,
                reminderId,
                intent,
                NotificationBroadcastReceiver.getCorrectlyFlag()
            )
        }
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(intent)
    }
}