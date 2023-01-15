package com.octopus.inc.domain.usecases

import com.octopus.inc.domain.models.Note
import com.octopus.inc.domain.repository.NoteRepository

class GetNoteListUseCase(private val noteRepository: NoteRepository) {
    suspend fun execute(): List<Note> {
        return noteRepository.getNotes()
    }
}