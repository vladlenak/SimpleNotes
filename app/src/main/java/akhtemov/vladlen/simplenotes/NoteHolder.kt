package akhtemov.vladlen.simplenotes

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {
    private var titleTextView = view.findViewById<TextView>(R.id.title_text_view)
    private var descriptionTextView = view.findViewById<TextView>(R.id.description_text_view)
    private var dateTextView = view.findViewById<TextView>(R.id.date_text_view)

    fun setData(note: Note) {
        titleTextView.text = note.title
        descriptionTextView.text = note.description
        dateTextView.text = note.date

        itemView.setOnClickListener {
            val intent = Intent(context, EditNoteActivity::class.java).apply {
                putExtra(IntentConstant.TITLE_KEY, note.title)
                putExtra(IntentConstant.DESCRIPTION_KEY, note.description)
                putExtra(IntentConstant.DATE_KEY, note.date)
            }
            context.startActivity(intent)
        }
    }
}