package akhtemov.vladlen.simplenotes.ui

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.databinding.ActivityAddNoteBinding
import akhtemov.vladlen.simplenotes.mylibraries.CalendarHelper
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.material.datepicker.MaterialDatePicker

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding

    private val isDueDateEmpty = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        isDueDateEmpty.observe(this) { isDueDateEmpty ->
            if (isDueDateEmpty) {
                binding.clearDueDate.visibility = View.GONE
            } else {
                binding.clearDueDate.visibility = View.VISIBLE
            }
        }

//        if (binding.dueDate.text.toString().isNotEmpty()) {
//            isDueDateEmpty.value = false
//        }

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save_button -> {
                    onClickSaveNoteButton()
                    true
                }
                else -> false
            }
        }

        binding.dueDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.due_date)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.show(supportFragmentManager, DATE_PICKER_TAG)

            datePicker.addOnPositiveButtonClickListener {
                binding.dueDate.setText(CalendarHelper().getDateFromMilliseconds(it))
                isDueDateEmpty.value = false
            }
        }

        binding.clearDueDate.setOnClickListener {
            binding.dueDate.setText("")
            isDueDateEmpty.value = true
        }
    }

    private fun onClickSaveNoteButton() {
        if (TextUtils.isEmpty(binding.title.text.toString())) {
            Toast.makeText(this, R.string.title_cannot_be_empty, Toast.LENGTH_SHORT).show()
        } else {
            val title = binding.title.text.toString()
            val desc = binding.description.text.toString()
            val deadline = binding.dueDate.text.toString()

            val replyIntent = Intent()
            replyIntent.putExtra(TITLE_EXTRA_REPLY, title)
            replyIntent.putExtra(DESCRIPTION_EXTRA_REPLY, desc)
            replyIntent.putExtra(DEADLINE_EXTRA_REPLY, deadline)

            setResult(Activity.RESULT_OK, replyIntent)

            finish()
        }
    }

    companion object {
        const val TITLE_EXTRA_REPLY = "title_extra_reply"
        const val DESCRIPTION_EXTRA_REPLY = "description_extra_reply"
        const val DEADLINE_EXTRA_REPLY = "deadline_extra_reply"

        const val DATE_PICKER_TAG = "date_picker_tag"
    }
}