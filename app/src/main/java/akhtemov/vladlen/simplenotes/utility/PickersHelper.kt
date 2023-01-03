package akhtemov.vladlen.simplenotes.utility

import akhtemov.vladlen.simplenotes.R
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class PickersHelper {
    companion object {
        const val DATE_PICKER_TAG = "date_picker_tag"
        const val TIME_PICKER_TAG = "time_picker_tag"

        fun getDatePicker(childFragmentManager: FragmentManager): MaterialDatePicker<Long> {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.set_due_date)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.show(childFragmentManager, DATE_PICKER_TAG)

            return datePicker
        }

        fun getTimePicker(childFragmentManager: FragmentManager): MaterialTimePicker {
            val timePicker = MaterialTimePicker.Builder()
                .setTitleText(R.string.set_due_time)
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.show(childFragmentManager, TIME_PICKER_TAG)

            return timePicker
        }
    }
}