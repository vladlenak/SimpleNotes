package akhtemov.vladlen.simplenotes.presentation.fragments.notelist.adapter

import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.databinding.ListItemNoteBinding
import akhtemov.vladlen.simplenotes.presentation.model.NoteView
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class NoteListAdapter(private var noteList: MutableList<NoteView>) :
    RecyclerView.Adapter<NoteViewHolder>() {

    private var noteCallbacks: NoteListCallbacks? = null

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

    fun setNoteCallbacks(noteCallbacks: NoteListCallbacks) {
        this.noteCallbacks = noteCallbacks
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addNotes(notes: List<NoteView>) {
        noteList.clear()
        noteList.addAll(notes)
        notifyDataSetChanged()
    }
}