package akhtemov.vladlen.simplenotes.presentation.mapper

import akhtemov.vladlen.simplenotes.presentation.model.NoteView
import com.octopus.inc.domain.models.Note
import javax.inject.Inject

class NoteMapper @Inject constructor(): Mapper<NoteView, Note> {
    override fun mapToView(type: Note): NoteView {
        return NoteView(
            id = type.id,
            title = type.title,
            desc = type.desc,
            date = type.date,
            time = type.time
        )
    }

    override fun mapFromView(type: NoteView): Note {
        return Note(
            id = type.id,
            title = type.title,
            desc = type.desc,
            date = type.date,
            time = type.time
        )
    }
}