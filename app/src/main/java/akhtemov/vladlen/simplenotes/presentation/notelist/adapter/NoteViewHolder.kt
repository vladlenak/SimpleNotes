package akhtemov.vladlen.simplenotes.presentation.notelist.adapter

import akhtemov.vladlen.simplenotes.databinding.ListItemNoteBinding
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.octopus.inc.domain.models.NoteModel

class NoteViewHolder(private val binding: ListItemNoteBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var noteCallbacks: NoteListCallbacks? = null

    fun bind(note: NoteModel) {
        binding.titleTextView.text = note.title

        if (note.desc.isEmpty()) {
            binding.descriptionTextView.visibility = View.GONE
        } else {
            binding.descriptionTextView.visibility = View.VISIBLE
            binding.descriptionTextView.text = note.desc
        }

        binding.dateTextView.text = note.date
        binding.timeTv.text = note.time

        binding.noteContainer.setOnClickListener {
            noteCallbacks?.onClickNoteContainer(note)
        }
    }

    fun setNoteCallbacks(noteCallbacks: NoteListCallbacks) {
        this.noteCallbacks = noteCallbacks
    }
}