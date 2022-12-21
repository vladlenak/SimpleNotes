package akhtemov.vladlen.simplenotes.presentation.notedetail

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.databinding.FragmentNoteDetailBinding
import akhtemov.vladlen.simplenotes.presentation.notelist.NoteListFragment
import akhtemov.vladlen.simplenotes.utility.CalendarHelper
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.octopus.inc.domain.models.NoteModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoteDetailBinding
    private val viewModel: NoteDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false)

        init()
        addObservers()
        addListeners()

        return binding.root
    }

    private fun init() {
        val arguments = arguments
        val noteId = arguments?.getString("noteId")

        if (noteId != null) {
            viewModel.getNote(noteId)
        }
    }

    private fun addObservers() {
        viewModel.note.observe(viewLifecycleOwner) { note ->
            binding.title.setText(note.title)
            binding.description.setText(note.desc)
            binding.dueDate.setText(note.date)

            checkClearDueDateButtonVisibility()
        }

        viewModel.isDueDateEmpty.observe(viewLifecycleOwner) { isDueDateEmpty ->
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

            datePicker.show(childFragmentManager, NoteListFragment.DATE_PICKER_TAG)

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
        val title = viewModel.note.value?.title
        val desc = viewModel.note.value?.desc
        val deadline = viewModel.note.value?.date

        if (TextUtils.isEmpty(binding.title.text.toString())) {
            Toast.makeText(context, R.string.title_cannot_be_empty, Toast.LENGTH_SHORT).show()
        } else if (newTitle != title || newDesc != desc || newDeadline != deadline) {
            if (id != null && title != null && desc != null && deadline != null) {
                val note = NoteModel(
                    id = id,
                    title = newTitle,
                    desc = newDesc,
                    date = newDeadline
                )

                viewModel.saveNote(note)
                findNavController().navigateUp()
            }
        } else if (newTitle == title && newDesc == desc && newDeadline == deadline) {
            findNavController().navigateUp()
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