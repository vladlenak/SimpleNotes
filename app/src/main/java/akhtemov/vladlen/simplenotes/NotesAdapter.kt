package akhtemov.vladlen.simplenotes

import akhtemov.vladlen.musicspeakercontrol.mylibraries.BaseAdapter
import akhtemov.vladlen.musicspeakercontrol.mylibraries.BaseViewHolder
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class NotesAdapter : BaseAdapter<Note>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Note> {
        return MyHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false))
    }

    class MyHolder(view: View) : BaseViewHolder<Note>(view) {
        private var titleTextView = view.findViewById<TextView>(R.id.title_text_view)
        private var descriptionTextView = view.findViewById<TextView>(R.id.description_text_view)
        private var dateTextView = view.findViewById<TextView>(R.id.date_text_view)

        override fun bind(model: Note) {
            titleTextView.text = model.title
            descriptionTextView.text = model.description
            dateTextView.text = getCorrectDateFormat(model.date)


        }

        private fun getCorrectDateFormat(dateFromDatabase: String) : String {
            val simpleDateFormat = SimpleDateFormat(Const.DATE_PATTERN)
            val date = simpleDateFormat.parse(dateFromDatabase)
            val calendar = GregorianCalendar()
            calendar.time = date

            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            val dayOfMonthNow = GregorianCalendar.getInstance().get(Calendar.DAY_OF_MONTH)
            val monthNow = GregorianCalendar.getInstance().get(Calendar.MONTH)
            val yearNow = GregorianCalendar.getInstance().get(Calendar.YEAR)

            if (dayOfMonth == dayOfMonthNow && month == monthNow && year == yearNow) {
                return calendar.get(Calendar.HOUR_OF_DAY).toString() + ":" + calendar.get(Calendar.MINUTE)
            } else {
                return calendar.get(Calendar.DAY_OF_MONTH).toString() + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR)
            }
        }

//        private fun dateFormatForToday(dateFromDatabase: String) : String {
//            val simpleDateFormat = SimpleDateFormat("dd-M-yyyy hh:mm")
//            val date = simpleDateFormat.parse(dateFromDatabase)
//            val calendar = GregorianCalendar()
//            calendar.time = date
//
//            val dateString = calendar.get(Calendar.HOUR).toString() + ":" + calendar.get(Calendar.MINUTE)
//            return dateString
//        }

    }

}