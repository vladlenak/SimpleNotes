package akhtemov.vladlen.simplenotes.utility


import java.text.SimpleDateFormat
import java.util.*

const val DATE_PATTERN = "yyyy-M-dd"
const val TIME_PATTERN = "hh:mm"

class CalendarHelper {
    fun getDateFromMilliseconds(timeInMillis: Long): String {
        val calendar = GregorianCalendar()
        calendar.timeInMillis = timeInMillis

        val simpleDateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
        simpleDateFormat.calendar = calendar

        return simpleDateFormat.format(calendar.time)
    }

    fun getTimeFromMilliseconds(timeInMillis: Long): String {
        val calendar = GregorianCalendar()
        calendar.timeInMillis = timeInMillis

        val simpleDateFormat = SimpleDateFormat(TIME_PATTERN, Locale.getDefault())
        simpleDateFormat.calendar = calendar

        return simpleDateFormat.format(calendar.time)
    }
}