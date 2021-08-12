package akhtemov.vladlen.simplenotes.activities

import akhtemov.vladlen.simplenotes.IntentConstant
import akhtemov.vladlen.simplenotes.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

class EditNoteActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        titleEditText = findViewById(R.id.title_edit_text)
        descriptionEditText = findViewById(R.id.description_edit_text)

        getIntentData()
    }

    private fun getIntentData() {
        val intent = intent

        titleEditText.setText(intent.getStringExtra(IntentConstant.TITLE_KEY))
        descriptionEditText.setText(intent.getStringExtra(IntentConstant.DESCRIPTION_KEY))
    }

}