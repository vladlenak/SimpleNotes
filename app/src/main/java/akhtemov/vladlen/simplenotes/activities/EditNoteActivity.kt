package akhtemov.vladlen.simplenotes.activities

import akhtemov.vladlen.simplenotes.Const
import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.database.DatabaseManager
import akhtemov.vladlen.simplenotes.databinding.ActivityEditNoteBinding
import akhtemov.vladlen.simplenotes.databinding.ActivityMainBinding
import akhtemov.vladlen.simplenotes.mylibraries.CalendarHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText

private const val TAG = "EditNoteActivity"

class EditNoteActivity : AppCompatActivity() {
    private var id = ""
    private var title: String? = ""
    private var desc: String? = ""

    private lateinit var binding: ActivityEditNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getIntentData()
        onMenuItemClickListener()
    }

    private fun getIntentData() {
        val intent = intent

        id = intent.getStringExtra(Const.POSITION)!!
        title = intent.getStringExtra(Const.TITLE_KEY)
        desc = intent.getStringExtra(Const.DESCRIPTION_KEY)

        binding.titleTextField.setText(title)
        binding.descriptionTextField.setText(desc)
    }

    private fun onMenuItemClickListener() {
        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.save_button -> {
                    onClickSaveNoteButton()

                    true
                }
                else -> false
            }
        }
    }

    private fun onClickSaveNoteButton() {
        val newTitle = binding.titleTextField.text.toString()
        val newDesc = binding.descriptionTextField.text.toString()

        if (title != newTitle || desc != newDesc) {
            val newDate = CalendarHelper().getCurrentDate(Const.DATE_PATTERN)
            val databaseManager = DatabaseManager(this)

            databaseManager.openDatabase()
            databaseManager.updateItem(newTitle, newDesc, newDate, id)
            databaseManager.closeDatabase()
        }

        finish()
    }

}