package akhtemov.vladlen.simplenotes.ui

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.databinding.ActivityEditNoteBinding
import akhtemov.vladlen.simplenotes.mylibraries.CalendarHelper
import akhtemov.vladlen.simplenotes.viewmodel.EditNoteViewModel
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.octopus.inc.domain.models.NoteModel

class EditNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditNoteBinding
    private val viewModel: EditNoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        init()
        addObservers()
        addListeners()
        checkClearDueDateButtonVisibility()
    }

    private fun init() {
        val intent = intent
        val noteId = intent.getStringExtra(ID_EXTRA_REPLY).toString()
        val noteTitle = intent.getStringExtra(TITLE_EXTRA_REPLY).toString()
        val noteDescription = intent.getStringExtra(DESCRIPTION_EXTRA_REPLY).toString()
        val noteDueDate = intent.getStringExtra(DEADLINE_EXTRA_REPLY).toString()
        val note = NoteModel(noteId, noteTitle, noteDescription, noteDueDate)
        viewModel.note.value = note
    }

    private fun addObservers() {
        viewModel.note.observe(this) { note ->
            binding.title.setText(note.title)
            binding.description.setText(note.desc)
            binding.dueDate.setText(note.date)
        }

        viewModel.isDueDateEmpty.observe(this) { isDueDateEmpty ->
            if (isDueDateEmpty) {
                binding.clearDueDate.visibility = View.GONE
            } else {
                binding.clearDueDate.visibility = View.VISIBLE
            }
        }
    }

    private fun addListeners() {
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save_button -> {
                    onClickSaveNoteButton()
                    true
                }

//                R.id.delete_note_button -> {
//                    onClickDeleteNoteButton()
//                    true
//                }
                else -> false
            }
        }

        binding.dueDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.set_due_date)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.show(supportFragmentManager, MainActivity.DATE_PICKER_TAG)

            datePicker.addOnPositiveButtonClickListener {
                binding.dueDate.setText(CalendarHelper().getDateFromMilliseconds(it))
                viewModel.isDueDateEmpty.value = false
            }
        }

        binding.clearDueDate.setOnClickListener {
            binding.dueDate.setText("")
            viewModel.isDueDateEmpty.value = true
        }
    }

    private fun checkClearDueDateButtonVisibility() {
        if (viewModel.note.value?.date?.isNotEmpty() == true) {
            viewModel.isDueDateEmpty.value = false
        }
    }

    private fun onClickSaveNoteButton() {
        val newTitle = binding.title.text.toString()
        val newDesc = binding.description.text.toString()
        val newDeadline = binding.dueDate.text.toString()

        val id = viewModel.note.value?.id
        val desc = viewModel.note.value?.desc
        val deadline = viewModel.note.value?.date

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

    private fun onClickDeleteNoteButton() {
        // TODO deleteNote fun
    }

    companion object {
        const val ID_EXTRA_REPLY = "id_extra_reply"
        const val TITLE_EXTRA_REPLY = "title_extra_reply"
        const val DESCRIPTION_EXTRA_REPLY = "description_extra_reply"
        const val DEADLINE_EXTRA_REPLY = "date_extra_reply"
    }
}