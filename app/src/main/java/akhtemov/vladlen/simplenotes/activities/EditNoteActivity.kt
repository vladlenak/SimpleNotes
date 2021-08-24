package akhtemov.vladlen.simplenotes.activities

import akhtemov.vladlen.simplenotes.Const
import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.database.DatabaseManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

private const val TAG = "EditNoteActivity"

class EditNoteActivity : AppCompatActivity() {

    private lateinit var titleEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText
    private var id = ""

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
        Log.d(TAG, "getIntentData: $id")
        titleEditText.setText(intent.getStringExtra(Const.TITLE_KEY))
        descriptionEditText.setText(intent.getStringExtra(Const.DESCRIPTION_KEY))
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
        val title = titleEditText.text.toString()
        val desc = descriptionEditText.text.toString()
        val date = "дата (в разработке)"
        val databaseManager = DatabaseManager(this)

        databaseManager.openDatabase()
        databaseManager.updateItem(title, desc, date, id)
        databaseManager.closeDatabase()

        finish()
    }

}