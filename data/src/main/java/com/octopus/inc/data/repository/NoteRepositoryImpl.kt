package com.octopus.inc.data.repository

import com.octopus.inc.data.room.NoteRoomImpl
import com.octopus.inc.domain.models.Note
import com.octopus.inc.domain.repository.NoteRepository
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(private val noteRoomImpl: NoteRoomImpl) :
    NoteRepository {

    override suspend fun getNotes(): List<Note> {
        return noteRoomImpl.getNotes()
    }

    override suspend fun getNote(noteId: String): Note {
        return noteRoomImpl.getNote(noteId)
    }

    override suspend fun insertNote(note: Note) {
        noteRoomImpl.insertNote(note)
    }

    override suspend fun updateNote(note: Note) {
        noteRoomImpl.updateNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        noteRoomImpl.deleteNote(note)
    }
}