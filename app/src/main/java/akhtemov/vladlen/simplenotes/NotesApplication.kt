package akhtemov.vladlen.simplenotes

import akhtemov.vladlen.simplenotes.persistence.NoteRepository
import akhtemov.vladlen.simplenotes.persistence.NoteRoomDatabase
import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class NotesApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { NoteRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { NoteRepository(database.noteDao()) }
}