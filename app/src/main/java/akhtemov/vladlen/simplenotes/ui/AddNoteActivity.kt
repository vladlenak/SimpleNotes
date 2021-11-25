package akhtemov.vladlen.simplenotes.ui

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.databinding.ActivityAddNoteBinding
import android.app.Activity
import android.content.Intent
import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save_button -> {
                    onClickSaveNoteButton()
                    true
                }
                else -> false
            }
        }

        binding.datePickerTextView.setOnClickListener {
            // A date range picker can be instantiated with
            val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText(R.string.add_due_date)
                // Opens the date picker with today's date selected.
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            // To show the picker to the user:
            datePicker.show(supportFragmentManager, DATE_PICKER_TAG)

            datePicker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = it
                binding.datePickerTextView.text = calendar.time.toString()
            }
        }
    }

    private fun onClickSaveNoteButton() {
        val replyIntent = Intent()

        if (TextUtils.isEmpty(binding.titleTextField.text.toString())) {
            Toast.makeText(this, R.string.title_cannot_be_empty, Toast.LENGTH_SHORT).show()
        } else {
            val title = binding.titleTextField.text.toString()
            val desc = binding.descriptionTextField.text.toString()
            val deadline = binding.datePickerTextView.text.toString()

            replyIntent.putExtra(TITLE_EXTRA_REPLY, title)
            replyIntent.putExtra(DESCRIPTION_EXTRA_REPLY, desc)
            replyIntent.putExtra(DEADLINE_EXTRA_REPLY, deadline)
            setResult(Activity.RESULT_OK, replyIntent)

            finish()
        }
    }

    companion object {
        const val TAG = "AddNoteActivity"

        const val TITLE_EXTRA_REPLY = "title_extra_reply"
        const val DESCRIPTION_EXTRA_REPLY = "description_extra_reply"
        const val DEADLINE_EXTRA_REPLY = "deadline_extra_reply"

        const val DATE_PICKER_TAG = "date_picker_tag"
    }
}