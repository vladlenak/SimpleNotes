package akhtemov.vladlen.simplenotes.activities

import akhtemov.vladlen.musicspeakercontrol.mylibraries.BaseAdapterCallback
import akhtemov.vladlen.simplenotes.Const
import akhtemov.vladlen.simplenotes.Note
import akhtemov.vladlen.simplenotes.NotesAdapter
import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.database.DatabaseManager
import akhtemov.vladlen.simplenotes.databinding.ActivityMainBinding
import akhtemov.vladlen.simplenotes.mylibraries.CalendarHelper
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.net.NetworkCapabilities
import android.net.ConnectivityManager

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private var databaseManager = DatabaseManager(this)
    private var notesAdapter = NotesAdapter()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = notesAdapter
        }

        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(binding.notesRecyclerView)

        checkInternetConnection()
    }

    private fun addRecyclerViewItemClickListener() {
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

    private fun addFabClickListener() {
        binding.addFab.setOnClickListener {
            val title = getString(R.string.new_note)
            val description = ""
            val data = CalendarHelper().getCurrentDate(Const.DATE_PATTERN)

            databaseManager.insertToDatabase(title, description, data)

            fillAdapter()
        }
    }

    private fun fillAdapter() {
        notesAdapter.updateItems(databaseManager.readDatabaseData())
    }

    private fun checkInternetConnection() {
        val status = checkForInternet(this)

        if (status) {
            binding.internetStatusTextView.visibility = View.GONE
            addFabClickListener()
            addRecyclerViewItemClickListener()
        } else {
            binding.addFab.visibility = View.GONE
            binding.notesRecyclerView.visibility = View.GONE
            binding.internetStatusTextView.visibility = View.VISIBLE
            binding.internetStatusTextView.text = "Нет интернета"
        }
    }

    private fun checkForInternet(context: Context): Boolean {
        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Returns a Network object corresponding to
        // the currently active default data network.
        val network = connectivityManager.activeNetwork ?: return false

        // Representation of the capabilities of an active network.
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            // Indicates this network uses a Wi-Fi transport,
            // or WiFi has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            // Indicates this network uses a Cellular transport. or
            // Cellular has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            // else return false
            else -> false
        }
    }
}