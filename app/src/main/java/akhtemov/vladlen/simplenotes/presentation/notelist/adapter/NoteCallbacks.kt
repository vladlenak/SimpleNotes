package akhtemov.vladlen.simplenotes.presentation.notelist.adapter

import com.octopus.inc.domain.models.NoteModel

interface NoteCallbacks {
    fun onClickNoteContainer(note: NoteModel)
}