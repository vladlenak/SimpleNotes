package akhtemov.vladlen.simplenotes.ui

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.databinding.ActivityEditNoteBinding
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class EditNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditNoteBinding

    private lateinit var id: String
    private lateinit var title: String
    private lateinit var desc: String
    private lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save_button -> {
                    onClickSaveNoteButton()
                    true
                }
                else -> false
            }
        }

        val intent = intent

        id = intent.getStringExtra(ID).toString()
        title = intent.getStringExtra(TITLE).toString()
        desc = intent.getStringExtra(DESCRIPTION).toString()
        date = intent.getStringExtra(DATE).toString()

        binding.titleTextField.setText(title)
        binding.descriptionTextField.setText(desc)
    }

    private fun onClickSaveNoteButton() {
        val replyIntent = Intent()

        val newTitle = binding.titleTextField.text.toString()
        val newDesc = binding.descriptionTextField.text.toString()

        if (newTitle != title || newDesc != desc) {
            replyIntent.putExtra(ID, id)
            replyIntent.putExtra(TITLE, newTitle)
            replyIntent.putExtra(DESCRIPTION, newDesc)
            setResult(Activity.RESULT_OK, replyIntent)
        } else {
            setResult(Activity.RESULT_CANCELED, replyIntent)
        }

        finish()
    }

    companion object {
        const val ID = "id_edit"
        const val TITLE = "title_edit"
        const val DESCRIPTION = "description_edit"
        const val DATE = "date_edit"
    }
}