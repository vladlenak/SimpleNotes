package akhtemov.vladlen.simplenotes.presentation.fragments.notelist.adapter

import akhtemov.vladlen.simplenotes.presentation.model.NoteView

interface NoteListCallbacks {
    fun onClickNoteContainer(note: NoteView)
}