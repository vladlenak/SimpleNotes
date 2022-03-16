package akhtemov.vladlen.simplenotes.adapter

import akhtemov.vladlen.simplenotes.databinding.ListItemNoteBinding
import akhtemov.vladlen.simplenotes.db.Note
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class NoteViewHolder(private val binding: ListItemNoteBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var noteCallbacks: NoteCallbacks? = null

    fun bind(note: Note) {
        binding.titleTextView.text = note.title

        if (note.description == "") {
            binding.descriptionTextView.visibility = View.GONE
        } else {
            binding.descriptionTextView.visibility = View.VISIBLE
            binding.descriptionTextView.text = note.description
        }

        binding.dateTextView.text = note.date

        binding.noteContainer.setOnClickListener {
            noteCallbacks?.onClickNoteContainer(note)
        }
    }

    fun setNoteCallbacks(noteCallbacks: NoteCallbacks) {
        this.noteCallbacks = noteCallbacks
    }
}