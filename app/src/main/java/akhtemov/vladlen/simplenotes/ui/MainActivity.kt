package akhtemov.vladlen.simplenotes.ui

import akhtemov.vladlen.simplenotes.mylibraries.BaseAdapterCallback
import akhtemov.vladlen.simplenotes.NotesApplication
import akhtemov.vladlen.simplenotes.adapter.NoteListAdapter
import akhtemov.vladlen.simplenotes.databinding.ActivityMainBinding
import akhtemov.vladlen.simplenotes.persistence.Note
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private var noteListAdapter = NoteListAdapter()

    private lateinit var binding: ActivityMainBinding

    private val addNoteActivityRequestCode = 1
    private val editNoteActivityRequestCode = 2

    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((application as NotesApplication).repository)
    }

    lateinit var myNotes: List<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = noteListAdapter
        }

        noteViewModel.allNotes.observe(this) { notes ->
            notes.let { noteListAdapter.submitList(it) }
            myNotes = notes
        }

        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(binding.notesRecyclerView)

        binding.addNoteFab.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivityForResult(intent, addNoteActivityRequestCode)
        }

        addRecyclerViewItemClickListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == addNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val titleReplay = intentData?.getStringExtra(AddNoteActivity.TITLE_EXTRA_REPLY)!!
            val descriptionReplay = intentData.getStringExtra(AddNoteActivity.DESCRIPTION_EXTRA_REPLY)!!
            val deadline = intentData.getStringExtra(AddNoteActivity.DEADLINE_EXTRA_REPLY)!!

            val note = Note(titleReplay, descriptionReplay, deadline)

            noteViewModel.insert(note)
        } else if (requestCode == editNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val myId = intentData?.getStringExtra(EditNoteActivity.ID_EXTRA_REPLY)!!
            val myTitle = intentData.getStringExtra(EditNoteActivity.TITLE_EXTRA_REPLY)!!
            val myDesc = intentData.getStringExtra(EditNoteActivity.DESCRIPTION_EXTRA_REPLY)!!
            val newDeadline = intentData.getStringExtra(EditNoteActivity.DEADLINE_EXTRA_REPLY)!!

            val note = Note(myId, myTitle, myDesc, newDeadline)
            noteViewModel.updateNote(note)
        } else {
            //Toast.makeText(applicationContext, R.string.empty_not_saved, Toast.LENGTH_LONG).show()
        }
    }

    private fun addRecyclerViewItemClickListener() {
        noteListAdapter.attachCallback(object: BaseAdapterCallback<Note> {
            override fun onItemClick(position: Int) {
                val myIntent = Intent(this@MainActivity, EditNoteActivity::class.java).apply {
                    val note = myNotes[position]

                    putExtra(EditNoteActivity.ID_EXTRA_REPLY, note.id)
                    putExtra(EditNoteActivity.TITLE_EXTRA_REPLY, note.title)
                    putExtra(EditNoteActivity.DESCRIPTION_EXTRA_REPLY, note.description)
                    putExtra(EditNoteActivity.DEADLINE_EXTRA_REPLY, note.date)
                }

                startActivityForResult(myIntent, editNoteActivityRequestCode)
            }
        })
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
                val note: Note = myNotes[pos]

                noteViewModel.deleteNote(note)
            }
        })
    }

}