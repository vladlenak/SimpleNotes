package com.octopus.inc.domain.repository

import com.octopus.inc.domain.models.NoteModel

interface NoteRepository {
    fun saveNote(note: NoteModel)
    fun getNoteList() : List<NoteModel>
    fun getNote(noteId: String)
}