package akhtemov.vladlen.simplenotes.presentation.notelist.adapter

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.databinding.ListItemNoteBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.octopus.inc.domain.models.NoteModel

class NoteAdapter(var noteList: MutableList<NoteModel>) : RecyclerView.Adapter<NoteViewHolder>() {

    private var noteCallbacks: NoteCallbacks? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_note, parent, false)
        return NoteViewHolder(ListItemNoteBinding.bind(view))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(noteList[position])
        noteCallbacks?.let { noteCallbacks ->
            holder.setNoteCallbacks(noteCallbacks)
        }
    }

    override fun getItemCount() = noteList.size

    fun setNoteCallbacks(noteCallbacks: NoteCallbacks) {
        this.noteCallbacks = noteCallbacks
    }

    fun addNotes(notes: List<NoteModel>) {
        noteList.clear()
        noteList.addAll(notes)
        notifyDataSetChanged()
    }
}