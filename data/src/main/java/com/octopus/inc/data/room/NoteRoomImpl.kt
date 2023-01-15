package com.octopus.inc.data.room

import com.octopus.inc.data.mapper.NoteMapper
import com.octopus.inc.domain.models.Note
import javax.inject.Inject

class NoteRoomImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val noteMapper: NoteMapper
) : NoteRoom {

    override suspend fun insertNote(note: Note) {
        noteDao.insert(noteMapper.mapToEntity(note))
    }

    override suspend fun getNotes(): List<Note> {
        val notes = noteDao.getNotes()
        val newNoteList = mutableListOf<Note>()

        notes.forEach { note ->
            newNoteList.add(noteMapper.mapFromEntity(note))
        }

        return newNoteList
    }

    override suspend fun getNote(noteId: String): Note {
        return noteMapper.mapFromEntity(noteDao.getNote(noteId))
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(noteMapper.mapToEntity(note))
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(noteMapper.mapToEntity(note))
    }
}