package akhtemov.vladlen.simplenotes.di

import akhtemov.vladlen.simplenotes.presentation.mapper.NoteMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideNoteMapper(): NoteMapper {
        return NoteMapper()
    }

}