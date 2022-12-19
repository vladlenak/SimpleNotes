package akhtemov.vladlen.simplenotes.di

import com.octopus.inc.domain.repository.NoteRepository
import com.octopus.inc.domain.usecases.DeleteNoteUseCase
import com.octopus.inc.domain.usecases.GetNoteListUseCase
import com.octopus.inc.domain.usecases.SaveNoteUseCase
import com.octopus.inc.domain.usecases.UpdateNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetNoteListUseCase(noteRepository: NoteRepository): GetNoteListUseCase {
        return GetNoteListUseCase(noteRepository = noteRepository)
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