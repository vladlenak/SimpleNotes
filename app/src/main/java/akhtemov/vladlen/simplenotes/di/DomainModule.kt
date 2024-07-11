package akhtemov.vladlen.simplenotes.di

import com.octopus.inc.domain.repository.NoteRepository
import com.octopus.inc.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    fun provideGetNoteListUseCase(noteRepository: NoteRepository): GetNoteListUseCase {
        return GetNoteListUseCase(noteRepository = noteRepository)
    }

    @Provides
    fun provideGetNoteUseCase(noteRepository: NoteRepository): GetNoteUseCase {
        return GetNoteUseCase(noteRepository = noteRepository)
    }

    @Provides
    fun provideDeleteNoteUseCase(noteRepository: NoteRepository): DeleteNoteUseCase {
        return DeleteNoteUseCase(noteRepository = noteRepository)
    }

    @Provides
    fun provideSaveNoteUseCase(noteRepository: NoteRepository): SaveNoteUseCase {
        return SaveNoteUseCase(noteRepository = noteRepository)
    }

    @Provides
    fun provideUpdateNoteUseCase(noteRepository: NoteRepository): UpdateNoteUseCase {
        return UpdateNoteUseCase(noteRepository = noteRepository)
    }

}