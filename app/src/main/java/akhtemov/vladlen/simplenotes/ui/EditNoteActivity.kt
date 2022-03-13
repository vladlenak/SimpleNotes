package akhtemov.vladlen.simplenotes.ui

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.databinding.ActivityEditNoteBinding
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

class EditNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditNoteBinding
    private lateinit var id: String
    private lateinit var title: String
    private lateinit var desc: String
    private lateinit var deadline: String

    private val isDueDateEmpty = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = intent
        id = intent.getStringExtra(ID_EXTRA_REPLY).toString()
        title = intent.getStringExtra(TITLE_EXTRA_REPLY).toString()
        desc = intent.getStringExtra(DESCRIPTION_EXTRA_REPLY).toString()
        deadline = intent.getStringExtra(DEADLINE_EXTRA_REPLY).toString()

        binding.title.setText(title)
        binding.description.setText(desc)
        binding.dueDate.setText(deadline)

        isDueDateEmpty.observe(this) { isDueDateEmpty ->
            if (isDueDateEmpty) {
                binding.clearDueDate.visibility = View.GONE
            } else {
                binding.clearDueDate.visibility = View.VISIBLE
            }
        }

        if (deadline.isNotEmpty()) {
            isDueDateEmpty.value = false
        }

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

            datePicker.show(supportFragmentManager, AddNoteActivity.DATE_PICKER_TAG)

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
        val newTitle = binding.title.text.toString()
        val newDesc = binding.description.text.toString()
        val newDeadline = binding.dueDate.text.toString()

        if (TextUtils.isEmpty(binding.title.text.toString())) {
            Toast.makeText(this, R.string.title_cannot_be_empty, Toast.LENGTH_SHORT).show()
        } else if (newTitle != title || newDesc != desc || newDeadline != deadline) {
            val replyIntent = Intent()
            replyIntent.putExtra(ID_EXTRA_REPLY, id)
            replyIntent.putExtra(TITLE_EXTRA_REPLY, newTitle)
            replyIntent.putExtra(DESCRIPTION_EXTRA_REPLY, newDesc)
            replyIntent.putExtra(DEADLINE_EXTRA_REPLY, newDeadline)

            setResult(Activity.RESULT_OK, replyIntent)

            finish()
        } else if (newTitle == title && newDesc == desc && newDeadline == deadline) {
            finish()
        }
    }

    companion object {
        const val ID_EXTRA_REPLY = "id_extra_reply"
        const val TITLE_EXTRA_REPLY = "title_extra_reply"
        const val DESCRIPTION_EXTRA_REPLY = "description_extra_reply"
        const val DEADLINE_EXTRA_REPLY = "date_extra_reply"
    }
}