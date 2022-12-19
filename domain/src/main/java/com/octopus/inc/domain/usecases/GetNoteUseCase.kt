package com.octopus.inc.domain.usecases

import com.octopus.inc.domain.repository.NoteRepository

class GetNoteUseCase(private val noteRepository: NoteRepository) {
    fun execute(noteId: String) {
        noteRepository.getNote(noteId)
    }
}