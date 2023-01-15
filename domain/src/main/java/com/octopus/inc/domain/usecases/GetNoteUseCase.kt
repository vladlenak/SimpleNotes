package com.octopus.inc.domain.usecases

import com.octopus.inc.domain.models.Note
import com.octopus.inc.domain.repository.NoteRepository

class GetNoteUseCase(private val noteRepository: NoteRepository) {
    suspend fun execute(noteId: String): Note {
        return noteRepository.getNote(noteId)
    }
}