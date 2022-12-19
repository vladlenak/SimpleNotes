package com.octopus.inc.data.repository

import com.octopus.inc.data.room.NoteRoomImpl
import com.octopus.inc.domain.models.NoteModel
import com.octopus.inc.domain.repository.NoteRepository

class NoteRepositoryImpl(private val noteRoomImpl: NoteRoomImpl): NoteRepository {

    override suspend fun getNotes(): List<NoteModel> {
        return noteRoomImpl.getNotes()
    }

    override suspend fun insertNote(note: NoteModel) {
        noteRoomImpl.insertNote(note)
    }

    override suspend fun updateNote(note: NoteModel) {
        noteRoomImpl.updateNote(note)
    }

    override suspend fun deleteNote(note: NoteModel) {
        noteRoomImpl.deleteNote(note)
    }
}