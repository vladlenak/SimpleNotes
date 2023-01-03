package akhtemov.vladlen.simplenotes.presentation.notedetail

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.databinding.FragmentNoteDetailBinding
import akhtemov.vladlen.simplenotes.presentation.notelist.NoteListFragment
import akhtemov.vladlen.simplenotes.utility.CalendarHelper
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
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
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

            if (note.date.isEmpty()) {
                binding.setDueDateChip.text = getString(R.string.set_due_date)
            } else {
                binding.setDueDateChip.text = note.date
                binding.setDueDateChip.isCloseIconVisible = true
            }

            if (note.time.isEmpty()) {
                binding.setDueTimeChip.text = getString(R.string.set_due_time)
            } else {
                binding.setDueTimeChip.text = note.time
                binding.setDueTimeChip.isCloseIconVisible = true
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

        binding.setDueDateChip.setOnClickListener {
            // TODO Вынести в PickerHelper
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.set_due_date)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.show(childFragmentManager, NoteListFragment.DATE_PICKER_TAG)

            datePicker.addOnPositiveButtonClickListener {
                binding.setDueDateChip.text = CalendarHelper().getDateFromMilliseconds(it)
                binding.setDueDateChip.isCloseIconVisible = true
            }
        }

        // TODO Вынести в PickerHelper
        binding.setDueTimeChip.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTitleText(R.string.set_due_time)
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.show(childFragmentManager, NoteListFragment.TIME_PICKER_TAG)

            timePicker.addOnPositiveButtonClickListener {
                binding.setDueTimeChip.apply {
                    val time = "${timePicker.hour}:${timePicker.minute}"

                    text = time
                    isCloseIconVisible = true
                }
            }
        }

        binding.setDueDateChip.setOnCloseIconClickListener {
            binding.setDueDateChip.text = getString(R.string.set_due_date)
            binding.setDueDateChip.isCloseIconVisible = false
        }

        binding.setDueTimeChip.setOnCloseIconClickListener {
            binding.setDueTimeChip.apply {
                text = getString(R.string.set_due_time)
                isCloseIconVisible = false
            }
        }
    }

    private fun onClickSaveNoteButton() {
        val newTitle = binding.title.text.toString()
        val newDesc = binding.description.text.toString()
        val newDate = binding.setDueDateChip.text.toString()
        val newTime = binding.setDueTimeChip.text.toString()

        viewModel.note.value?.let { note ->
            if (TextUtils.isEmpty(binding.title.text.toString())) {
                Toast.makeText(context, R.string.title_cannot_be_empty, Toast.LENGTH_SHORT).show()
            } else if (newTitle != note.title || newDesc != note.desc || newDate != note.date || newTime != note.time) {
                if (note.id != null && note.title != null && note.desc != null && note.date != null && note.time != null) {
                    val note = NoteModel(
                        id = note.id,
                        title = newTitle,
                        desc = newDesc,
                        date = newDate,
                        time = newTime
                    )

                    if (note.date == getString(R.string.set_due_date)) {
                        note.date = ""
                    }

                    if (note.time == getString(R.string.set_due_time)) {
                        note.time = ""
                    }

                    if (note.time.isNotEmpty()) {
                        if (note.date.isNotEmpty()) {
                            viewModel.saveNote(note)
                            findNavController().navigateUp()
                        } else {
                            Toast.makeText(context, R.string.date_cannot_be_empty, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        viewModel.saveNote(note)
                        findNavController().navigateUp()
                    }
                }
            } else if (newTitle == note.title && newDesc == note.desc && newDate == note.date && newTime == note.time) {
                findNavController().navigateUp()
            }
        }
    }

    private fun onClickDeleteNoteButton() {
        // TODO deleteNote fun
    }
}