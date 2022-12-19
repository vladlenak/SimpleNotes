package com.octopus.inc.domain.usecases

import com.octopus.inc.domain.models.NoteModel
import com.octopus.inc.domain.repository.NoteRepository

class UpdateNoteUseCase(private val noteRepository: NoteRepository) {
    suspend fun execute(noteModel: NoteModel) {
        noteRepository.updateNote(noteModel)
    }
}