package com.octopus.inc.domain.usecases

import com.octopus.inc.domain.models.Note
import com.octopus.inc.domain.repository.NoteRepository

class SaveNoteUseCase(private val noteRepository: NoteRepository) {
    suspend fun execute(note: Note) {
        noteRepository.insertNote(note)
    }
}