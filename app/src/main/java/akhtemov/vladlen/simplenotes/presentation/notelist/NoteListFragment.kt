package akhtemov.vladlen.simplenotes.presentation.notelist

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.databinding.FragmentNoteListBinding
import akhtemov.vladlen.simplenotes.presentation.dialogs.DeleteDialog
import akhtemov.vladlen.simplenotes.presentation.dialogs.DeleteDialogCallbacks
import akhtemov.vladlen.simplenotes.presentation.notelist.adapter.NoteAdapter
import akhtemov.vladlen.simplenotes.presentation.notelist.adapter.NoteCallbacks
import akhtemov.vladlen.simplenotes.presentation.notifications.RemindersManager
import akhtemov.vladlen.simplenotes.utility.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octopus.inc.domain.models.NoteModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class NoteListFragment : Fragment(), NoteCallbacks, DeleteDialogCallbacks {

    private lateinit var binding: FragmentNoteListBinding
    private val viewModel: NoteViewModel by viewModels()
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
        viewModel.setNotes()
    }

    override fun onClickDeleteDialogYes(position: Int) {
        viewModel.deleteNoteByPosition(position)
    }

    override fun onClickDeleteDialogNo() {
        viewModel.setNotes()
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



    private fun addObservers() {
        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            noteAdapter.addNotes(sortListByDateThenTime(notes.toMutableList()))
            addNotesWithTimeToAlarmReceiver(notes)
        }
    }

    private fun sortListByDateThenTime(list: MutableList<NoteModel>): List<NoteModel> {
        list.toMutableList().sortWith(compareBy<NoteModel> { it.date }.thenBy { it.time })
        return list
    }

    private fun addNotesWithTimeToAlarmReceiver(noteList: List<NoteModel>) {
        // RemindersManager.stopReminder(requireContext())
        val dateToday = DateHelper.getDateNow()
        var i = 1
        for (note in noteList) {
            if (dateToday == note.date) {
                if (note.time.isNotEmpty()) {
                    RemindersManager.startReminder(
                        context = requireContext(),
                        reminderTitle = note.title,
                        reminderDesc = note.desc,
                        reminderTime = note.time,
                        reminderId = i
                    )
                    i++
                }
            }
        }
    }



    private fun addListeners() {
        binding.addNoteFab.setOnClickListener {
            showAddNoteContainer()
        }

        binding.setDueDateChip.setOnClickListener {
            PickersHelper.getDatePicker(childFragmentManager).addOnPositiveButtonClickListener {
                binding.setDueDateChip.apply {
                    text = CalendarHelper().getDateFromMilliseconds(it)
                    isCloseIconVisible = true
                }
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

        binding.saveNote.setOnClickListener {
            saveNote()
        }
    }

    private fun showAddNoteContainer() {
        binding.createNoteContainer.visibility = View.VISIBLE
        binding.addNoteFab.visibility = View.GONE
        binding.noteTitle.requestFocus()
        showKeyboard(binding.noteTitle)
    }

    private fun saveNote() {
        createNoteModel()?.let { noteModel ->
            hideKeyboard()
            viewModel.insertNote(noteModel)
            hideAddNoteContainer()
        }
    }

    private fun createNoteModel(): NoteModel? {
        val noteModel = NoteModel(
            id = UUID.randomUUID().toString(),
            title = binding.noteTitle.text.toString()
        )
        val noteDate = binding.setDueDateChip.text.toString()
        val noteTime = binding.setDueTimeChip.text.toString()

        if (noteDate != getString(R.string.set_due_date)) noteModel.date = noteDate
        if (noteTime != getString(R.string.set_due_time)) noteModel.time = noteTime

        if(noteModel.title.isEmpty()) {
            Toast.makeText(context, R.string.title_cannot_be_empty, Toast.LENGTH_SHORT).show()
            return null
        }
        if (noteModel.time.isNotEmpty() && noteModel.date.isEmpty()) {
            Toast.makeText(context, R.string.date_cannot_be_empty, Toast.LENGTH_SHORT).show()
            return null
        }

        return noteModel
    }

    private fun hideAddNoteContainer() {
        binding.noteTitle.text.clear()
        binding.setDueDateChip.text = getString(R.string.set_due_date)
        binding.setDueTimeChip.text = getString(R.string.set_due_time)
        binding.createNoteContainer.visibility = View.GONE
        binding.addNoteFab.visibility = View.VISIBLE
    }
}