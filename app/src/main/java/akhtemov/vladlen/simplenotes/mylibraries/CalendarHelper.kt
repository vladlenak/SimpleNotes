package akhtemov.vladlen.simplenotes.mylibraries

import android.icu.text.SimpleDateFormat
import android.icu.util.GregorianCalendar
import java.util.*

const val DATE_PATTERN = "yyyy-M-dd"

class CalendarHelper {
    fun getDateFromMilliseconds(timeInMillis: Long): String {
        val calendar = GregorianCalendar()
        calendar.timeInMillis = timeInMillis

        val simpleDateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
        simpleDateFormat.calendar = calendar

        return simpleDateFormat.format(calendar.time)
    }
}