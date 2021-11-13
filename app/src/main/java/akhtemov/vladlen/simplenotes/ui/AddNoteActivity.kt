package akhtemov.vladlen.simplenotes.ui

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.databinding.ActivityEditNoteBinding
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditNoteBinding

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
    }

    private fun onClickSaveNoteButton() {
        val replyIntent = Intent()

        if (TextUtils.isEmpty(binding.titleTextField.text.toString())) {
            setResult(Activity.RESULT_CANCELED, replyIntent)
        } else {
            val newTitle = binding.titleTextField.text.toString()
            val newDesc = binding.descriptionTextField.text.toString()

            replyIntent.putExtra(TITLE_EXTRA_REPLY, newTitle)
            replyIntent.putExtra(DESCRIPTION_EXTRA_REPLY, newDesc)
            setResult(Activity.RESULT_OK, replyIntent)
        }

        finish()
    }

    companion object {
        const val TAG = "AddNoteActivity"
        const val TITLE_EXTRA_REPLY = "title_extra_reply"
        const val DESCRIPTION_EXTRA_REPLY = "description_extra_reply"
    }
}