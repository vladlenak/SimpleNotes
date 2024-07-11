package com.octopus.inc.data.repository

import com.octopus.inc.domain.models.Note
import com.octopus.inc.domain.repository.NoteRepository
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteLocalDataSource: NoteLocalDataSourceImpl
) : NoteRepository {

    override suspend fun getNotes(): List<Note> {
        return noteLocalDataSource.getNotes()
    }

    override suspend fun getNote(noteId: String): Note {
        return noteLocalDataSource.getNote(noteId)
    }

    override suspend fun insertNote(note: Note) {
        noteLocalDataSource.insertNote(note)
    }

    override suspend fun updateNote(note: Note) {
        noteLocalDataSource.updateNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        noteLocalDataSource.deleteNote(note)
    }

}