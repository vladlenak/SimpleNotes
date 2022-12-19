package akhtemov.vladlen.simplenotes.adapter

import com.octopus.inc.domain.models.NoteModel

interface NoteCallbacks {
    fun onClickNoteContainer(note: NoteModel)
}