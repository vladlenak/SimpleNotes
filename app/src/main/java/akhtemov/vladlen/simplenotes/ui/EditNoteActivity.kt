package akhtemov.vladlen.simplenotes.ui

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.databinding.ActivityEditNoteBinding
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast

class EditNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditNoteBinding

    private lateinit var id: String
    private lateinit var title: String
    private lateinit var desc: String
    private lateinit var deadline: String

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

        id = intent.getStringExtra(ID_EXTRA_REPLY).toString()
        title = intent.getStringExtra(TITLE_EXTRA_REPLY).toString()
        desc = intent.getStringExtra(DESCRIPTION_EXTRA_REPLY).toString()
        deadline = intent.getStringExtra(DEADLINE_EXTRA_REPLY).toString()

        binding.titleTextField.setText(title)
        binding.descriptionTextField.setText(desc)
        binding.deadlineEditText.setText(deadline)
    }

    private fun onClickSaveNoteButton() {
        val replyIntent = Intent()

        val newTitle = binding.titleTextField.text.toString()
        val newDesc = binding.descriptionTextField.text.toString()
        val newDeadline = binding.deadlineEditText.text.toString()

        if (TextUtils.isEmpty(binding.titleTextField.text.toString())) {
            Toast.makeText(this, R.string.title_cannot_be_empty, Toast.LENGTH_SHORT).show()
        } else if (newTitle != title || newDesc != desc || newDeadline != deadline) {
            replyIntent.putExtra(ID_EXTRA_REPLY, id)
            replyIntent.putExtra(TITLE_EXTRA_REPLY, newTitle)
            replyIntent.putExtra(DESCRIPTION_EXTRA_REPLY, newDesc)
            replyIntent.putExtra(DEADLINE_EXTRA_REPLY, newDeadline)

            setResult(Activity.RESULT_OK, replyIntent)

            finish()
        } else if (newTitle == title && newDesc == desc && newDeadline == deadline) {
            finish()
        }
    }

    companion object {
        const val ID_EXTRA_REPLY = "id_extra_reply"
        const val TITLE_EXTRA_REPLY = "title_extra_reply"
        const val DESCRIPTION_EXTRA_REPLY = "description_extra_reply"
        const val DEADLINE_EXTRA_REPLY = "date_extra_reply"
    }
}