package com.octopus.inc.data.room

import com.octopus.inc.domain.models.NoteModel

interface NoteRoom {
    suspend fun insertNote(note: NoteModel)
    suspend fun getNotes(): List<NoteModel>
    suspend fun getNote(noteId: String): NoteModel
    suspend fun updateNote(note: NoteModel)
    suspend fun deleteNote(note: NoteModel)
}