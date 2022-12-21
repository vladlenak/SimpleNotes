package akhtemov.vladlen.simplenotes.presentation.notelist

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.databinding.FragmentNoteListBinding
import akhtemov.vladlen.simplenotes.presentation.dialogs.DeleteDialog
import akhtemov.vladlen.simplenotes.presentation.dialogs.DeleteDialogCallbacks
import akhtemov.vladlen.simplenotes.presentation.notelist.adapter.NoteAdapter
import akhtemov.vladlen.simplenotes.presentation.notelist.adapter.NoteCallbacks
import akhtemov.vladlen.simplenotes.utility.CalendarHelper
import akhtemov.vladlen.simplenotes.utility.hideKeyboard
import akhtemov.vladlen.simplenotes.utility.showKeyboard
import android.os.Bundle
import android.text.TextUtils
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
import com.google.android.material.datepicker.MaterialDatePicker
import com.octopus.inc.domain.models.NoteModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class NoteListFragment: Fragment(), NoteCallbacks, DeleteDialogCallbacks {

    private lateinit var binding: FragmentNoteListBinding
    private val noteViewModel: NoteViewModel by viewModels()
    private val noteAdapter = NoteAdapter(mutableListOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteListBinding.inflate(inflater, container, false)

        init()
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

    private fun init() {
        noteAdapter.setNoteCallbacks(this)

        binding.notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = noteAdapter
        }

        noteViewModel.notes.observe(viewLifecycleOwner) { notes ->
            noteAdapter.addNotes(notes)
        }

        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(binding.notesRecyclerView)

        binding.addNoteFab.setOnClickListener {
            binding.createNoteContainer.visibility = View.VISIBLE
            binding.addNoteFab.visibility = View.GONE
            binding.noteTitle.requestFocus()
            showKeyboard(binding.noteTitle)
        }

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

    private fun addListeners() {
        binding.setDueDateChip.setOnClickListener {
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

        binding.createNote.setOnClickListener {
            if (!TextUtils.isEmpty(binding.noteTitle.text)) {
                hideKeyboard()

                val noteTitle = binding.noteTitle.text.toString()
                val noteDueDate = binding.setDueDateChip.text.toString()
                val note = NoteModel(
                    id = UUID.randomUUID().toString(),
                    title = noteTitle,
                    desc = "",
                    date = ""
                )

                if (noteDueDate != getString(R.string.set_due_date)) {
                    note.date = noteDueDate
                }

                noteViewModel.insert(note)

                binding.noteTitle.text.clear()
                binding.setDueDateChip.text = getString(R.string.set_due_date)
                binding.createNoteContainer.visibility = View.GONE
                binding.addNoteFab.visibility = View.VISIBLE
            } else {
                Toast.makeText(context, R.string.title_cannot_be_empty, Toast.LENGTH_SHORT).show()
            }
        }

        binding.setDueDateChip.setOnCloseIconClickListener {
            binding.setDueDateChip.apply {
                text = getString(R.string.set_due_date)
                isCloseIconVisible = false
            }
        }
    }

    private fun getSwapMg() : ItemTouchHelper {
        return ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val notePosition = viewHolder.adapterPosition
                DeleteDialog.showDeleteDialog(notePosition, this@NoteListFragment, childFragmentManager)
            }
        })
    }

    companion object {
        const val DATE_PICKER_TAG = "date_picker_tag"
    }

    override fun onClickDeleteDialogYes(position: Int) {
        noteViewModel.deleteNoteByPosition(position)
    }

    override fun onClickDeleteDialogNo() {
        noteViewModel.setNotes()
    }
}