package akhtemov.vladlen.simplenotes.di

import android.content.Context
import androidx.room.Room
import com.octopus.inc.data.repository.NoteRepositoryImpl
import com.octopus.inc.data.room.NoteDao
import com.octopus.inc.data.room.NoteRoom
import com.octopus.inc.data.room.NoteRoomDatabase
import com.octopus.inc.data.room.NoteRoomImpl
import com.octopus.inc.domain.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideNoteRepositoryImpl(noteRoomImpl: NoteRoomImpl): NoteRepository {
        return NoteRepositoryImpl(noteRoomImpl)
    }

    @Provides
    fun provideNoteRoomImpl(noteDao: NoteDao): NoteRoom {
        return NoteRoomImpl(noteDao)
    }

    @Provides
    fun provideNoteDao(noteRoomDatabase: NoteRoomDatabase): NoteDao {
        return noteRoomDatabase.noteDao()
    }

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): NoteRoomDatabase {
        return Room
            .databaseBuilder(
                context,
                NoteRoomDatabase::class.java,
                NoteRoomDatabase.NOTE_ROOM_DATABASE_NAME
            )
            .addMigrations(NoteRoomDatabase.migration_1_2)
            .build()
    }
}