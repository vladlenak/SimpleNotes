package akhtemov.vladlen.simplenotes.presentation.notelist.adapter

import com.octopus.inc.domain.models.NoteModel

interface NoteListCallbacks {
    fun onClickNoteContainer(note: NoteModel)
}