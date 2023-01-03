package com.octopus.inc.data.room

import com.octopus.inc.data.model.Note
import com.octopus.inc.domain.models.NoteModel
import javax.inject.Inject

class NoteRoomImpl @Inject constructor(private val noteDao: NoteDao): NoteRoom {

    override suspend fun insertNote(note: NoteModel) {
        noteDao.insert(mapToNote(note))
    }

    override suspend fun getNotes(): List<NoteModel> {
        val notes = noteDao.getNotes()
        val newNoteList = mutableListOf<NoteModel>()

        for (note in notes) {
            newNoteList.add(mapToNoteModel(note))
        }

        return newNoteList
    }

    override suspend fun getNote(noteId: String): NoteModel {
        return mapToNoteModel(noteDao.getNote(noteId))
    }

    override suspend fun updateNote(note: NoteModel) {
        noteDao.updateNote(mapToNote(note))
    }

    override suspend fun deleteNote(note: NoteModel) {
        noteDao.deleteNote(mapToNote(note))
    }

    // TODO проверить в правильном ли месте находяться mappers
    private fun mapToNote(noteModel: NoteModel): Note {
        return Note(
            id = noteModel.id,
            title = noteModel.title,
            description = noteModel.desc,
            date = noteModel.date,
            time = noteModel.time
        )
    }

    private fun mapToNoteModel(note: Note): NoteModel {
        return NoteModel(
            id = note.id,
            title = note.title,
            desc = note.description,
            date = note.date,
            time = note.time
        )
    }
}