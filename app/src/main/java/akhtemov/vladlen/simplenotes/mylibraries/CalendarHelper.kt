package akhtemov.vladlen.simplenotes.mylibraries

import akhtemov.vladlen.simplenotes.Const
import android.icu.text.SimpleDateFormat
import android.icu.util.GregorianCalendar
import android.icu.util.ULocale
import java.util.*

class CalendarHelper {
    fun getCurrentDate(pattern: String) : String {
        val calendar = GregorianCalendar()
        val simpleDateFormat = SimpleDateFormat(pattern)

        simpleDateFormat.calendar = calendar

        return simpleDateFormat.format(calendar.time)
    }
}