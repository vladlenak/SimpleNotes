package akhtemov.vladlen.simplenotes.mylibraries

import akhtemov.vladlen.simplenotes.Const
import android.icu.text.SimpleDateFormat
import android.icu.util.GregorianCalendar
import java.util.*

class CalendarHelper {
    fun getCurrentDate() : String {
        val calendar = GregorianCalendar()
        val simpleDateFormat = SimpleDateFormat(Const.DATE_PATTERN, Locale.getDefault())

        simpleDateFormat.calendar = calendar

        return simpleDateFormat.format(calendar.time)
    }
}