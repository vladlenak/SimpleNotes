package akhtemov.vladlen.simplenotes

import akhtemov.vladlen.simplenotes.database.DatabaseManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    
    private var databaseManager = DatabaseManager(this)
    private var notesRecyclerViewAdapter = NotesRecyclerViewAdapter(ArrayList(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val notesRecyclerView = findViewById<RecyclerView>(R.id.notes_recycler_view)

        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        notesRecyclerView.adapter = notesRecyclerViewAdapter
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
        notesRecyclerViewAdapter.update(databaseManager.readDatabaseData())
    }

    private fun getDate() : String {
        return ""
    }
}