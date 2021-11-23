package akhtemov.vladlen.simplenotes.adapter

import akhtemov.vladlen.simplenotes.mylibraries.BaseAdapterCallback
import akhtemov.vladlen.simplenotes.R
import akhtemov.vladlen.simplenotes.persistence.Note
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class NoteListAdapter : ListAdapter<Note, NoteListAdapter.NoteViewHolder>(NotesComparator()) {

    private var mCallback: BaseAdapterCallback<Note>? = null

    fun attachCallback(callback: BaseAdapterCallback<Note>) {
        this.mCallback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.title, current.description, current.date)

        holder.itemView.setOnClickListener {
            mCallback?.onItemClick(position)
        }
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.title_text_view)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.description_text_view)
        private val dateTextView: TextView = itemView.findViewById(R.id.date_text_view)

        fun bind(title: String?, description: String?, date: String?) {
            titleTextView.text = title
            descriptionTextView.text = description
            dateTextView.text = date
        }

        companion object {
            fun create(parent: ViewGroup): NoteViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.note_item, parent, false)
                return NoteViewHolder(view)
            }
        }
    }

    class NotesComparator : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.title == newItem.title
        }
    }
}