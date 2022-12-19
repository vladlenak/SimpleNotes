package com.octopus.inc.domain.repository

import com.octopus.inc.domain.models.NoteModel

interface NoteRepository {
    suspend fun insertNote(note: NoteModel)
    suspend fun getNotes() : List<NoteModel>
    suspend fun updateNote(note: NoteModel)
    suspend fun deleteNote(note: NoteModel)
}