package akhtemov.vladlen.simplenotes.db

class NoteRepository(private val noteDao: NoteDao) {
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    suspend fun getNotes(): List<Note> {
        return noteDao.getNotes()
    }
}