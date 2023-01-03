package com.octopus.inc.domain.usecases

import com.octopus.inc.domain.models.NoteModel
import com.octopus.inc.domain.repository.NoteRepository

class SaveNoteUseCase(private val noteRepository: NoteRepository) {
    suspend fun execute(note: NoteModel) {
        noteRepository.insertNote(note)
    }
}