package akhtemov.vladlen.simplenotes.di

import akhtemov.vladlen.simplenotes.db.NoteDao
import akhtemov.vladlen.simplenotes.db.NoteRepository
import akhtemov.vladlen.simplenotes.db.NoteRoomDatabase
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository {
        return NoteRepository(noteDao = noteDao)
    }

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): NoteRoomDatabase {
        return Room.databaseBuilder(
            context,
            NoteRoomDatabase::class.java,
            "note_database"
        ).build()
    }

    @Provides
    fun provideNoteDao(noteRoomDatabase: NoteRoomDatabase): NoteDao {
        return noteRoomDatabase.noteDao()
    }
}