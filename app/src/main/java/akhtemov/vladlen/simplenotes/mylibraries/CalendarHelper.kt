package akhtemov.vladlen.simplenotes.mylibraries

import android.icu.text.SimpleDateFormat
import android.icu.util.GregorianCalendar
import java.util.*

class CalendarHelper {
    fun getCurrentDate() : String {
        val calendar = GregorianCalendar()
        val simpleDateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())

        simpleDateFormat.calendar = calendar

        return simpleDateFormat.format(calendar.time)
    }

    companion object {
        const val DATE_PATTERN = "dd-M-yyyy HH:mm"
    }
}