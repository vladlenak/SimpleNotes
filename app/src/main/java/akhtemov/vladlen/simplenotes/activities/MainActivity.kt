package akhtemov.vladlen.simplenotes.activities

import akhtemov.vladlen.musicspeakercontrol.mylibraries.BaseAdapterCallback
import akhtemov.vladlen.simplenotes.IntentConstant
import akhtemov.vladlen.simplenotes.Note
import akhtemov.vladlen.simplenotes.NotesAdapter
import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.database.DatabaseManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    
    private var databaseManager = DatabaseManager(this)
    private var notesAdapter = NotesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val notesRecyclerView = findViewById<RecyclerView>(R.id.notes_recycler_view)

        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        notesRecyclerView.adapter = notesAdapter

        notesAdapter.attachCallback(object: BaseAdapterCallback<Note> {
            override fun onItemClick(model: Note, view: View) {
                var myIntent = Intent(this@MainActivity, EditNoteActivity::class.java).apply {
                    putExtra(IntentConstant.TITLE_KEY, model.title)
                    putExtra(IntentConstant.DESCRIPTION_KEY, model.description)
                    putExtra(IntentConstant.DATE_KEY, model.date)
                }

                startActivity(myIntent)
            }

            override fun onLongClick(model: Note, view: View): Boolean {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onResume() {
        super.onResume()
        databaseManager.openDatabase()
        fillAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseManager.closeDatabase()
    }

    fun onClickAddFab(view: View) {
        val title = getString(R.string.new_note)
        val description = ""
        val data = getDate()

        databaseManager.insertToDatabase(title, description, data)
        fillAdapter()
    }

    private fun fillAdapter() {
        notesAdapter.updateItems(databaseManager.readDatabaseData())
    }

    private fun getDate() : String {
        return ""
    }
}