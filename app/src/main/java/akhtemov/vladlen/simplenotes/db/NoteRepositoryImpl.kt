package akhtemov.vladlen.simplenotes.db

import com.octopus.inc.domain.models.NoteModel
import com.octopus.inc.domain.repository.NoteRepository

class NoteRepositoryImpl(private val noteDao: NoteDao): NoteRepository {

    override suspend fun getNotes(): List<NoteModel> {
        val notes = noteDao.getNotes()
        val newNoteList = mutableListOf<NoteModel>()

        for (note in notes) {
            newNoteList.add(
                NoteModel(
                    id = note.id,
                    title = note.title,
                    desc = note.description,
                    date = note.date
                )
            )
        }

        return newNoteList
    }

    override suspend fun insertNote(note: NoteModel) {
        noteDao.insert(mapToNote(note))
    }

    override suspend fun updateNote(note: NoteModel) {
        noteDao.updateNote(mapToNote(note))
    }

    override suspend fun deleteNote(note: NoteModel) {
        noteDao.deleteNote(mapToNote(note))
    }

    private fun mapToNote(noteModel: NoteModel): Note {
        return Note(
            id = noteModel.id,
            title = noteModel.title,
            description = noteModel.desc,
            date = noteModel.date
        )
    }

    private fun mapToNoteModel(note: Note): NoteModel {
        return NoteModel(
            id = note.id,
            title = note.title,
            desc = note.description,
            date = note.date
        )
    }
}