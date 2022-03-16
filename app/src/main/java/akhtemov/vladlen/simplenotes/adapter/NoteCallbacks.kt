package akhtemov.vladlen.simplenotes.adapter

import akhtemov.vladlen.simplenotes.db.Note

interface NoteCallbacks {
    fun onClickNoteContainer(note: Note)
}