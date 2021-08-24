package akhtemov.vladlen.simplenotes.activities

import akhtemov.vladlen.musicspeakercontrol.mylibraries.BaseAdapterCallback
import akhtemov.vladlen.simplenotes.Const
import akhtemov.vladlen.simplenotes.Note
import akhtemov.vladlen.simplenotes.NotesAdapter
import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.database.DatabaseManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.os.Build

import android.net.NetworkCapabilities

import android.net.ConnectivityManager
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private var databaseManager = DatabaseManager(this)
    private var notesAdapter = NotesAdapter()
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var addFab: FloatingActionButton
    private lateinit var internetStatusTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()


        val status = checkForInternet(this)

        if (status) {
            internetStatusTextView.visibility = View.GONE
            addFabClickListener()
            addRecyclerViewItemClickListener()
        } else {
            addFab.visibility = View.GONE
            notesRecyclerView.visibility = View.GONE
            internetStatusTextView.visibility = View.VISIBLE
            internetStatusTextView.text = "Нет интернета"
        }

    }

    private fun init() {
        notesRecyclerView = findViewById(R.id.notes_recycler_view)
        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(notesRecyclerView)
        notesRecyclerView.adapter = notesAdapter

        addFab = findViewById(R.id.add_fab)
        internetStatusTextView = findViewById(R.id.internet_status_text_view)
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
        addFab.setOnClickListener {
            val title = getString(R.string.new_note)
            val description = ""
            val data = getDate()

            databaseManager.insertToDatabase(title, description, data)

            fillAdapter()
        }
    }

    private fun fillAdapter() {
        notesAdapter.updateItems(databaseManager.readDatabaseData())
    }

    private fun getDate() : String {
        return "дата (в разработке)"
    }

    private fun checkInternetConnection() {

    }

    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

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
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i(TAG, "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i(TAG, "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i(TAG, "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}