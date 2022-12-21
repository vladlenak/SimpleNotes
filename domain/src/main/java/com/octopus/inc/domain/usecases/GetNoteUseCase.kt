package com.octopus.inc.domain.usecases

import com.octopus.inc.domain.models.NoteModel
import com.octopus.inc.domain.repository.NoteRepository

class GetNoteUseCase(private val noteRepository: NoteRepository) {
    suspend fun execute(noteId: String): NoteModel{
        return noteRepository.getNote(noteId)
    }
}