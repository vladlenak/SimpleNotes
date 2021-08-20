package akhtemov.vladlen.simplenotes.activities

import akhtemov.vladlen.musicspeakercontrol.mylibraries.BaseAdapterCallback
import akhtemov.vladlen.simplenotes.Const
import akhtemov.vladlen.simplenotes.Note
import akhtemov.vladlen.simplenotes.NotesAdapter
import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.database.DatabaseManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private var databaseManager = DatabaseManager(this)
    private var notesAdapter = NotesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        onRecyclerViewItemClickListener()
    }

    private fun init() {
        val notesRecyclerView = findViewById<RecyclerView>(R.id.notes_recycler_view)

        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(notesRecyclerView)
        notesRecyclerView.adapter = notesAdapter
    }

    private fun onRecyclerViewItemClickListener() {
        notesAdapter.attachCallback(object: BaseAdapterCallback<Note> {
            override fun onItemClick(model: Note, view: View) {
                val myIntent = Intent(this@MainActivity, EditNoteActivity::class.java).apply {
                    putExtra(Const.POSITION, model.id.toString())
                    putExtra(Const.TITLE_KEY, model.title)
                    putExtra(Const.DESCRIPTION_KEY, model.description)
                    putExtra(Const.DATE_KEY, model.date)
                }

                startActivity(myIntent)
            }

            override fun onLongClick(model: Note, view: View): Boolean {
                TODO("Not yet implemented")
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
                val list = databaseManager.readDatabaseData()
                val pos = viewHolder.adapterPosition
                val id = list[pos].id.toString()

                databaseManager.removeFromDatabase(id)
                notesAdapter.removeItem(viewHolder.adapterPosition)

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