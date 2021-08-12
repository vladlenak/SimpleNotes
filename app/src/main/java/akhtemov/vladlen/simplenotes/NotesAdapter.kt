package akhtemov.vladlen.simplenotes

import akhtemov.vladlen.musicspeakercontrol.mylibraries.BaseAdapter
import akhtemov.vladlen.musicspeakercontrol.mylibraries.BaseViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class NotesAdapter : BaseAdapter<Note>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Note> {
        return MyHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false))
    }

    class MyHolder(view: View) : BaseViewHolder<Note>(view) {
        private var titleTextView = view.findViewById<TextView>(R.id.title_text_view)
        private var descriptionTextView = view.findViewById<TextView>(R.id.description_text_view)
        private var dateTextView = view.findViewById<TextView>(R.id.date_text_view)

        override fun bind(model: Note) {
            titleTextView.text = model.title
            descriptionTextView.text = model.description
            dateTextView.text = model.date


        }

    }

}