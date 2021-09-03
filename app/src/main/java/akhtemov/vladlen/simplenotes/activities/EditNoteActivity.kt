package akhtemov.vladlen.simplenotes.activities

import akhtemov.vladlen.simplenotes.Const
import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.database.DatabaseManager
import akhtemov.vladlen.simplenotes.mylibraries.CalendarHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText

private const val TAG = "EditNoteActivity"

class EditNoteActivity : AppCompatActivity() {

    private lateinit var titleEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText

    private var id = ""
    private var title: String? = ""
    private var desc: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        titleEditText = findViewById(R.id.title_text_field)
        descriptionEditText = findViewById(R.id.description_text_field)

        getIntentData()
        onMenuItemClickListener()
    }

    private fun getIntentData() {
        val intent = intent

        id = intent.getStringExtra(Const.POSITION)!!
        title = intent.getStringExtra(Const.TITLE_KEY)
        desc = intent.getStringExtra(Const.DESCRIPTION_KEY)

        titleEditText.setText(title)
        descriptionEditText.setText(desc)
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
        val newTitle = titleEditText.text.toString()
        val newDesc = descriptionEditText.text.toString()

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