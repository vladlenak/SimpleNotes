package akhtemov.vladlen.simplenotes.presentation.notenotification

import akhtemov.vladlen.simplenotes.presentation.model.NoteView
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar
import java.util.Locale

object NoteNotificationManager {

    fun restartReminders(context: Context, noteList: List<NoteView>) {
        var i = 1
        noteList.forEach { note ->
            if (note.time.isNotEmpty() && note.date.isNotEmpty()) {
                stopReminder(context = context, reminderId = i)
                startReminder(context = context, reminderId = i, note = note)
                i++
            }
        }
    }

    private fun startReminder(context: Context, reminderId: Int, note: NoteView) {
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
            NoteNotificationBroadcastReceiver::class.java
        ).apply {
            putExtra(NoteNotificationBroadcastReceiver.NOTIFICATION_ID_EXTRA_ID, reminderId)
            putExtra(NoteNotificationBroadcastReceiver.NOTIFICATION_TITLE_EXTRA_ID, note.title)
            putExtra(NoteNotificationBroadcastReceiver.NOTIFICATION_DESC_EXTRA_ID, note.desc)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            reminderId,
            intent,
            NoteNotificationBroadcastReceiver.getCorrectlyFlag()
        )

        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DATE, 1)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent),
                    pendingIntent
                )
            } else {
                requestExactAlarmPermission(context)
            }
        } else {
            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent),
                pendingIntent
            )
        }
    }

    private fun stopReminder(context: Context, reminderId: Int) {
        val intent = Intent(context, NoteNotificationBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            reminderId,
            intent,
            NoteNotificationBroadcastReceiver.getCorrectlyFlag()
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    private fun requestExactAlarmPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            context.startActivity(intent)
        }
    }
}