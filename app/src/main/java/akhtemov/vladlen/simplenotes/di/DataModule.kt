package akhtemov.vladlen.simplenotes.di

import android.content.Context
import androidx.room.Room
import com.octopus.inc.data.mapper.NoteMapper
import com.octopus.inc.data.repository.NoteRepositoryImpl
import com.octopus.inc.data.room.NoteDao
import com.octopus.inc.data.repository.NoteLocalDataSource
import com.octopus.inc.data.room.NoteDatabase
import com.octopus.inc.data.repository.NoteLocalDataSourceImpl
import com.octopus.inc.domain.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideNoteRepositoryImpl(noteLocalDataSourceImpl: NoteLocalDataSourceImpl): NoteRepository {
        return NoteRepositoryImpl(noteLocalDataSourceImpl)
    }

    @Provides
    fun provideNoteLocalDataSourceImpl(
        noteDao: NoteDao,
        noteMapper: NoteMapper
    ): NoteLocalDataSource {
        return NoteLocalDataSourceImpl(noteDao, noteMapper)
    }

    @Provides
    fun provideNoteDao(noteRoomDatabase: NoteDatabase): NoteDao {
        return noteRoomDatabase.noteDao()
    }

    @Provides
    fun provideNoteMapper(): NoteMapper {
        return NoteMapper()
    }

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room
            .databaseBuilder(
                context,
                NoteDatabase::class.java,
                NoteDatabase.NOTE_DATABASE_NAME
            )
            .addMigrations(NoteDatabase.migration_1_2)
            .build()
    }

}