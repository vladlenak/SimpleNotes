package akhtemov.vladlen.simplenotes.ui

import akhtemov.vladlen.simplenotes.*
import akhtemov.vladlen.simplenotes.adapter.NoteListAdapter
import akhtemov.vladlen.simplenotes.databinding.ActivityMainBinding
import akhtemov.vladlen.simplenotes.mylibraries.CalendarHelper
import akhtemov.vladlen.simplenotes.persistence.Note
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.activity.viewModels

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private var noteListAdapter = NoteListAdapter()

    private lateinit var binding: ActivityMainBinding

    private val newWordActivityRequestCode = 1
    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((application as NotesApplication).repository)
    }

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
            // Update the cached copy of the words in the adapter.
            notes.let { noteListAdapter.submitList(it) }
        }

//        val swapHelper = getSwapMg()
//        swapHelper.attachToRecyclerView(binding.notesRecyclerView)

        binding.addFab.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val titleReplay = intentData?.getStringExtra(AddNoteActivity.TITLE_EXTRA_REPLY)!!
            val descriptionReplay = intentData?.getStringExtra(AddNoteActivity.DESCRIPTION_EXTRA_REPLY)!!
            val note = Note(titleReplay, descriptionReplay, CalendarHelper().getCurrentDate())
            noteViewModel.insert(note)
        } else {
            Toast.makeText(applicationContext, R.string.empty_not_saved, Toast.LENGTH_LONG).show()
        }
    }

//    private fun addRecyclerViewItemClickListener() {
//
//        noteListAdapter.attachCallback(object: BaseAdapterCallback<Note> {
//            override fun onItemClick(model: Note, view: View) {
//                val myIntent = Intent(this@MainActivity, EditNoteActivity::class.java).apply {
//                    putExtra(Const.POSITION, model.id.toString())
//                    putExtra(Const.TITLE_KEY, model.title)
//                    putExtra(Const.DESCRIPTION_KEY, model.description)
//                    putExtra(Const.DATE_KEY, model.date)
//                }
//
//                startActivity(myIntent)
//            }
//
//            override fun onLongClick(model: Note, view: View): Boolean {
//                TODO("Not yet implemented")
//            }
//        })
//    }

//    private fun getSwapMg() : ItemTouchHelper {
//        return ItemTouchHelper(object:ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                return false
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val list = databaseManager.readDatabaseData()
//                val pos = viewHolder.adapterPosition
//                val id = list[pos].id.toString()
//
//                databaseManager.removeFromDatabase(id)
//                adapter.removeItem(viewHolder.adapterPosition)
//
//            }
//        })
//    }


//    private fun fillAdapter() {
//        adapter.updateItems(databaseManager.readDatabaseData())
//    }

//    private fun checkInternetConnection() {
//        val status = checkForInternet(this)
//
//        if (status) {
//            binding.internetStatusTextView.visibility = View.GONE
//            addFabClickListener()
//            addRecyclerViewItemClickListener()
//        } else {
//            binding.addFab.visibility = View.GONE
//            binding.notesRecyclerView.visibility = View.GONE
//            binding.internetStatusTextView.visibility = View.VISIBLE
//            binding.internetStatusTextView.text = "Нет интернета"
//        }
//    }

//    private fun checkForInternet(context: Context): Boolean {
//        // register activity with the connectivity manager service
//        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//        // Returns a Network object corresponding to
//        // the currently active default data network.
//        val network = connectivityManager.activeNetwork ?: return false
//
//        // Representation of the capabilities of an active network.
//        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
//
//        return when {
//            // Indicates this network uses a Wi-Fi transport,
//            // or WiFi has network connectivity
//            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//
//            // Indicates this network uses a Cellular transport. or
//            // Cellular has network connectivity
//            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//
//            // else return false
//            else -> false
//        }
//    }
}