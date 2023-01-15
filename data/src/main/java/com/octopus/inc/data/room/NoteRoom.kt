package com.octopus.inc.data.room

import com.octopus.inc.domain.models.Note

interface NoteRoom {
    suspend fun insertNote(note: Note)
    suspend fun getNotes(): List<Note>
    suspend fun getNote(noteId: String): Note
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
}