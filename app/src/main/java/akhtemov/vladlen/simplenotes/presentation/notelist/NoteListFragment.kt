package akhtemov.vladlen.simplenotes.presentation.notelist

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.databinding.FragmentNoteListBinding
import akhtemov.vladlen.simplenotes.presentation.MainActivity
import akhtemov.vladlen.simplenotes.presentation.dialogs.DeleteDialog
import akhtemov.vladlen.simplenotes.presentation.dialogs.DeleteDialogCallbacks
import akhtemov.vladlen.simplenotes.presentation.notelist.adapter.NoteAdapter
import akhtemov.vladlen.simplenotes.presentation.notelist.adapter.NoteCallbacks
import akhtemov.vladlen.simplenotes.presentation.notifications.RemindersManager
import akhtemov.vladlen.simplenotes.utility.*
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.octopus.inc.domain.models.NoteModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class NoteListFragment : Fragment(), NoteCallbacks, DeleteDialogCallbacks {

    companion object {
        const val DATE_PICKER_TAG = "date_picker_tag"
        const val TIME_PICKER_TAG = "time_picker_tag"
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "chanel_id"
        const val CHANNEL_NAME = "chanel_name"
    }

    private lateinit var binding: FragmentNoteListBinding
    private val noteViewModel: NoteViewModel by viewModels()
    private val noteAdapter = NoteAdapter(mutableListOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteListBinding.inflate(inflater, container, false)

        PermissionHelper.checkPermission(requireContext(), requireActivity())
        NotificationHelper.createNotificationsChannels(requireContext())

        init()
        addObservers()
        addListeners()

        return binding.root
    }

    override fun onClickNoteContainer(note: NoteModel) {
        val action = NoteListFragmentDirections.actionNoteListFragmentToNoteDetailFragment(note.id)
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        noteViewModel.setNotes()
    }

    override fun onClickDeleteDialogYes(position: Int) {
        noteViewModel.deleteNoteByPosition(position)
    }

    override fun onClickDeleteDialogNo() {
        noteViewModel.setNotes()
    }

    private fun init() {
        noteAdapter.setNoteCallbacks(this)

        binding.notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = noteAdapter
        }

        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(binding.notesRecyclerView)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.createNoteContainer.visibility == View.VISIBLE) {
                    binding.createNoteContainer.visibility = View.GONE
                    binding.addNoteFab.visibility = View.VISIBLE
                } else {
                    activity?.finish()
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(requireActivity(), callback)
    }

    private fun addObservers() {
        noteViewModel.notes.observe(viewLifecycleOwner) { notes ->
            noteAdapter.addNotes(sortListByDateThenTime(notes.toMutableList()))
            addNotesWithTimeToAlarmReceiver(notes)
        }
    }

    private fun addListeners() {
        binding.setDueDateChip.setOnClickListener {
            // TODO Вынести в PickerHelper
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.set_due_date)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.show(childFragmentManager, DATE_PICKER_TAG)

            datePicker.addOnPositiveButtonClickListener {
                binding.setDueDateChip.apply {
                    text = CalendarHelper().getDateFromMilliseconds(it)
                    isCloseIconVisible = true
                }
            }
        }

        binding.setDueTimeChip.setOnClickListener {
            // TODO Вынести в PickerHelper
            val timePicker = MaterialTimePicker.Builder()
                .setTitleText(R.string.set_due_time)
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.show(childFragmentManager, TIME_PICKER_TAG)

            timePicker.addOnPositiveButtonClickListener {
                binding.setDueTimeChip.apply {
                    val time = "${timePicker.hour}:${timePicker.minute}"

                    text = time
                    isCloseIconVisible = true
                }
            }
        }

        binding.setDueDateChip.setOnCloseIconClickListener {
            binding.setDueDateChip.apply {
                text = getString(R.string.set_due_date)
                isCloseIconVisible = false
            }
        }

        binding.setDueTimeChip.setOnCloseIconClickListener {
            binding.setDueTimeChip.apply {
                text = getString(R.string.set_due_time)
                isCloseIconVisible = false
            }
        }

        binding.createNote.setOnClickListener {
            // TODO Вынести создание правильной модели в отдельный класс
            if (!TextUtils.isEmpty(binding.noteTitle.text)) {
                hideKeyboard()

                val noteTitle = binding.noteTitle.text.toString()
                val noteDate = binding.setDueDateChip.text.toString()
                val noteTime = binding.setDueTimeChip.text.toString()
                val note = NoteModel(
                    id = UUID.randomUUID().toString(),
                    title = noteTitle,
                    desc = "",
                    date = "",
                    time = ""
                )

                if (noteDate != getString(R.string.set_due_date)) {
                    note.date = noteDate
                }
                if (noteTime != getString(R.string.set_due_time)) {
                    note.time = noteTime
                }

                if (note.time.isNotEmpty()) {
                    if (note.date.isNotEmpty()) {
                        saveNoteToDb(note)
                    } else {
                        Toast.makeText(context, R.string.date_cannot_be_empty, Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    saveNoteToDb(note)
                }
            } else {
                Toast.makeText(context, R.string.title_cannot_be_empty, Toast.LENGTH_SHORT).show()
            }
        }

        binding.addNoteFab.setOnClickListener {
            binding.createNoteContainer.visibility = View.VISIBLE
            binding.addNoteFab.visibility = View.GONE
            binding.noteTitle.requestFocus()
            showKeyboard(binding.noteTitle)
        }
    }

    private fun getSwapMg(): ItemTouchHelper {
        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val notePosition = viewHolder.adapterPosition
                DeleteDialog.showDeleteDialog(
                    notePosition,
                    this@NoteListFragment,
                    childFragmentManager
                )
            }
        })
    }

    private fun sortListByDateThenTime(list: MutableList<NoteModel>): List<NoteModel> {
        list.toMutableList().sortWith(compareBy<NoteModel> { it.date }.thenBy { it.time })
        return list
    }

    private fun addNotesWithTimeToAlarmReceiver(noteList: List<NoteModel>) {
        // RemindersManager.stopReminder(requireContext())
        val dateToday = DateHelper.getDateNow()
        for (note in noteList) {
            if (dateToday == note.date) {
                if (note.time.isNotEmpty()) {
                    RemindersManager.startReminder(
                        requireContext(),
                        note.title,
                        note.desc,
                        note.time
                    )
                }
            }
        }
    }

    private fun saveNoteToDb(note: NoteModel) {
        noteViewModel.insert(note)

        binding.noteTitle.text.clear()
        binding.setDueDateChip.text = getString(R.string.set_due_date)
        binding.setDueTimeChip.text = getString(R.string.set_due_time)
        binding.createNoteContainer.visibility = View.GONE
        binding.addNoteFab.visibility = View.VISIBLE
    }
}