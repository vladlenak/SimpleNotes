package akhtemov.vladlen.simplenotes.presentation.notelist

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.databinding.FragmentNoteListBinding
import akhtemov.vladlen.simplenotes.extension.gone
import akhtemov.vladlen.simplenotes.extension.showToast
import akhtemov.vladlen.simplenotes.extension.visible
import akhtemov.vladlen.simplenotes.presentation.deletedialog.DeleteDialog
import akhtemov.vladlen.simplenotes.presentation.deletedialog.DeleteDialogCallbacks
import akhtemov.vladlen.simplenotes.presentation.model.NoteView
import akhtemov.vladlen.simplenotes.presentation.notelist.adapter.NoteListAdapter
import akhtemov.vladlen.simplenotes.presentation.notelist.adapter.NoteListCallbacks
import akhtemov.vladlen.simplenotes.presentation.notenotification.NoteNotificationBroadcastReceiver
import akhtemov.vladlen.simplenotes.presentation.notenotification.NoteNotificationManager
import akhtemov.vladlen.simplenotes.utility.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class NoteListFragment : Fragment(), NoteListCallbacks, DeleteDialogCallbacks {

    private lateinit var binding: FragmentNoteListBinding
    private val viewModel: NoteListViewModel by viewModels()
    private val noteAdapter = NoteListAdapter(mutableListOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteListBinding.inflate(inflater, container, false)

        PermissionHelper.checkPermission(requireContext(), requireActivity())
        NoteNotificationBroadcastReceiver.createNotificationsChannels(requireContext())

        init()
        addObservers()
        addListeners()

        return binding.root
    }

    override fun onClickNoteContainer(note: NoteView) {
        val action = NoteListFragmentDirections.actionNoteListFragmentToNoteDetailFragment(note.id)
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        viewModel.send(SetNotesEvent())
    }

    override fun onClickYesOnDeleteDialog(note: NoteView) {
        viewModel.send(DeleteNoteEvent(note))
    }

    override fun onClickNoOnDeleteDialog() {
        viewModel.send(SetNotesEvent())
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
                    binding.createNoteContainer.gone()
                    binding.addNoteFab.visible()
                } else {
                    activity?.finish()
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(requireActivity(), callback)
    }

    private fun getSwapMg(): ItemTouchHelper {
        return ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val notePosition = viewHolder.adapterPosition
                    viewModel.noteListState.value?.noteModelList?.get(notePosition)?.let { note ->
                        DeleteDialog.showDeleteDialog(
                            note = note,
                            callbacks = this@NoteListFragment,
                            fragmentManager = childFragmentManager
                        )
                    }
                }
            }
        )
    }


    private fun addObservers() {
        viewModel.noteListState.observe(viewLifecycleOwner) { noteListState ->
            noteAdapter.addNotes(sortListByDateThenTime(noteListState.noteModelList.toMutableList()))
            NoteNotificationManager.restartReminders(
                context = requireContext(),
                noteList = noteListState.noteModelList
            )
        }
    }

    private fun sortListByDateThenTime(list: MutableList<NoteView>): List<NoteView> {
        list.toMutableList().sortWith(compareBy<NoteView> { it.date }.thenBy { it.time })
        return list
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
                val time = "${timePicker.hour}:${timePicker.minute}"
                binding.setDueTimeChip.apply {
                    text = time
                    isCloseIconVisible = true
                }
                binding.setDueDateChip.apply {
                    text = CalendarHelper().getDateFromMilliseconds(Date().time)
                }
            }
        }

        binding.setDueDateChip.setOnCloseIconClickListener {
            binding.setDueDateChip.apply {
                text = getString(R.string.set_due_date)
                isCloseIconVisible = false
            }
            binding.setDueTimeChip.apply {
                text = getString(R.string.set_due_time)
                isCloseIconVisible = false
            }
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
        binding.createNoteContainer.visible()
        binding.addNoteFab.gone()
        binding.noteTitle.requestFocus()
        showKeyboard(binding.noteTitle)
    }

    private fun saveNote() {
        createNoteModel()?.let { noteModel ->
            hideKeyboard()
            viewModel.send(InsertNoteEvent(noteModel))
            hideAddNoteContainer()
        }
    }

    private fun createNoteModel(): NoteView? {
        val noteModel = NoteView(
            id = UUID.randomUUID().toString(),
            title = binding.noteTitle.text.toString()
        )
        val noteDate = binding.setDueDateChip.text.toString()
        val noteTime = binding.setDueTimeChip.text.toString()

        if (noteDate != getString(R.string.set_due_date)) noteModel.date = noteDate
        if (noteTime != getString(R.string.set_due_time)) noteModel.time = noteTime

        if (noteModel.title.isEmpty()) {
            context?.showToast(getString(R.string.title_cannot_be_empty))
            return null
        }
        if (noteModel.time.isNotEmpty() && noteModel.date.isEmpty()) {
            context?.showToast(getString(R.string.date_cannot_be_empty))
            return null
        }

        return noteModel
    }

    private fun hideAddNoteContainer() {
        binding.noteTitle.text.clear()
        binding.setDueDateChip.text = getString(R.string.set_due_date)
        binding.setDueTimeChip.text = getString(R.string.set_due_time)
        binding.createNoteContainer.gone()
        binding.addNoteFab.visible()
    }
}