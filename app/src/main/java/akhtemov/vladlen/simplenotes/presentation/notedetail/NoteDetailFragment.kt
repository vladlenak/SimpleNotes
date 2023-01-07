package akhtemov.vladlen.simplenotes.presentation.notedetail

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.databinding.FragmentNoteDetailBinding
import akhtemov.vladlen.simplenotes.presentation.dialogs.DeleteDialog
import akhtemov.vladlen.simplenotes.presentation.dialogs.DeleteDialogCallbacks
import akhtemov.vladlen.simplenotes.utility.CalendarHelper
import akhtemov.vladlen.simplenotes.utility.PickersHelper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.octopus.inc.domain.models.NoteModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteDetailFragment : Fragment(), DeleteDialogCallbacks {

    private lateinit var binding: FragmentNoteDetailBinding
    private val viewModel: NoteDetailViewModel by viewModels()
    private var noteId: String? = null

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

    override fun onClickDeleteDialogYes(note: NoteModel) {
        viewModel.deleteNote()
        findNavController().navigateUp()
    }

    override fun onClickDeleteDialogNo() {
        noteId?.let { noteId -> viewModel.getNote(noteId) }
    }

    private fun init() {
        val arguments = arguments
        noteId = arguments?.getString("noteId")
        if (noteId != null) viewModel.getNote(noteId!!)
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
                R.id.delete_note_button -> {
                    onClickDeleteNoteButton()
                    true
                }
                else -> false
            }
        }

        binding.setDueDateChip.setOnClickListener {
            PickersHelper.getDatePicker(childFragmentManager).addOnPositiveButtonClickListener {
                binding.setDueDateChip.text = CalendarHelper().getDateFromMilliseconds(it)
                binding.setDueDateChip.isCloseIconVisible = true
            }
        }

        binding.setDueTimeChip.setOnClickListener {
            val timePicker = PickersHelper.getTimePicker(childFragmentManager)
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
            binding.setDueTimeChip.text = getString(R.string.set_due_time)
            binding.setDueTimeChip.isCloseIconVisible = false
        }
    }

    private fun onClickSaveNoteButton() {
        createNoteModel()?.let { noteModel ->
            viewModel.updateNote(noteModel)
            findNavController().navigateUp()
        }
    }

    private fun createNoteModel(): NoteModel? {
        noteId?.let { noteId ->
            val noteModel = NoteModel(
                id = noteId,
                title = binding.title.text.toString(),
                desc = binding.description.text.toString()
            )
            val noteDate = binding.setDueDateChip.text.toString()
            val noteTime = binding.setDueTimeChip.text.toString()

            if (noteDate != getString(R.string.set_due_date)) noteModel.date = noteDate
            if (noteTime != getString(R.string.set_due_time)) noteModel.time = noteTime

            if (noteModel.title.isEmpty()) {
                Toast.makeText(context, R.string.title_cannot_be_empty, Toast.LENGTH_SHORT).show()
                return null
            }
            if (noteModel.time.isNotEmpty() && noteModel.date.isEmpty()) {
                Toast.makeText(context, R.string.date_cannot_be_empty, Toast.LENGTH_SHORT).show()
                return null
            }

            return noteModel
        }

        return null
    }

    private fun onClickDeleteNoteButton() {
        viewModel.note.value?.let { note ->
            DeleteDialog.showDeleteDialog(note, this, childFragmentManager)
        }
    }


}