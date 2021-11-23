package akhtemov.vladlen.simplenotes.persistence

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {

    val allNotes: Flow<List<Note>> = noteDao.getNotesOrderByDate()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }
}