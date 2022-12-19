package akhtemov.vladlen.simplenotes.ui

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.adapter.NoteAdapter
import akhtemov.vladlen.simplenotes.adapter.NoteCallbacks
import akhtemov.vladlen.simplenotes.databinding.ActivityMainBinding
import akhtemov.vladlen.simplenotes.hideKeyboard
import akhtemov.vladlen.simplenotes.mylibraries.CalendarHelper
import akhtemov.vladlen.simplenotes.showKeyboard
import akhtemov.vladlen.simplenotes.viewmodel.NoteViewModel
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.octopus.inc.domain.models.NoteModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NoteCallbacks {

    private lateinit var binding: ActivityMainBinding

    private val addNoteActivityRequestCode = 1
    private val editNoteActivityRequestCode = 2

    private val noteViewModel: NoteViewModel by viewModels()

    lateinit var myNotes: List<NoteModel>

    private val noteAdapter = NoteAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        noteAdapter.setNoteCallbacks(this)

        binding.notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = noteAdapter
        }

        noteViewModel.notes.observe(this) { notes ->
            noteAdapter.addNotes(notes)
            myNotes = notes
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
                    finish()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        binding.setDueDateChip.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.set_due_date)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.show(supportFragmentManager, DATE_PICKER_TAG)

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
//                val note = Note(noteTitle, "", "")
                val note = NoteModel(
                    id = "",
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
                Toast.makeText(this, R.string.title_cannot_be_empty, Toast.LENGTH_SHORT).show()
            }
        }

        binding.setDueDateChip.setOnCloseIconClickListener {
            binding.setDueDateChip.apply {
                text = getString(R.string.set_due_date)
                isCloseIconVisible = false
            }
        }
    }

    override fun onClickNoteContainer(note: NoteModel) {
        val myIntent = Intent(this@MainActivity, EditNoteActivity::class.java).apply {
            putExtra(EditNoteActivity.ID_EXTRA_REPLY, note.id)
            putExtra(EditNoteActivity.TITLE_EXTRA_REPLY, note.title)
            putExtra(EditNoteActivity.DESCRIPTION_EXTRA_REPLY, note.desc)
            putExtra(EditNoteActivity.DEADLINE_EXTRA_REPLY, note.date)
        }

        startActivityForResult(myIntent, editNoteActivityRequestCode)
    }

    override fun onResume() {
        super.onResume()
        noteViewModel.setNotes()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == addNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
//            val titleReplay = intentData?.getStringExtra(AddNoteActivity.TITLE_EXTRA_REPLY)!!
//            val descriptionReplay = intentData.getStringExtra(AddNoteActivity.DESCRIPTION_EXTRA_REPLY)!!
//            val deadline = intentData.getStringExtra(AddNoteActivity.DEADLINE_EXTRA_REPLY)!!
//
//            val note = Note(titleReplay, descriptionReplay, deadline)
//
//            noteViewModel.insert(note)
        } else if (requestCode == editNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val myId = intentData?.getStringExtra(EditNoteActivity.ID_EXTRA_REPLY)!!
            val myTitle = intentData.getStringExtra(EditNoteActivity.TITLE_EXTRA_REPLY)!!
            val myDesc = intentData.getStringExtra(EditNoteActivity.DESCRIPTION_EXTRA_REPLY)!!
            val newDeadline = intentData.getStringExtra(EditNoteActivity.DEADLINE_EXTRA_REPLY)!!

            val note = NoteModel(
                id = myId,
                title = myTitle,
                desc = myDesc,
                date = newDeadline
            )
            noteViewModel.updateNote(note)
        } else {
            //Toast.makeText(applicationContext, R.string.empty_not_saved, Toast.LENGTH_LONG).show()
        }
    }

    private fun getSwapMg() : ItemTouchHelper {
        return ItemTouchHelper(object:ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val note = myNotes[pos]
                noteViewModel.deleteNote(note)
            }
        })
    }

    companion object {
        const val DATE_PICKER_TAG = "date_picker_tag"
    }

}