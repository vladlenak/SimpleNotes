package akhtemov.vladlen.simplenotes

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class NotesRecyclerViewAdapter(private var notesArrayList: ArrayList<Note>, private val context: Context) : RecyclerView.Adapter<NoteHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NoteHolder(inflater.inflate(R.layout.note_item, parent, false), context)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.setData(notesArrayList[position])
    }

    override fun getItemCount(): Int {
        return notesArrayList.size
    }

    fun update(newNotesArrayList: ArrayList<Note>) {
        notesArrayList.clear()
        notesArrayList.addAll(newNotesArrayList)
        notifyDataSetChanged()
    }
}