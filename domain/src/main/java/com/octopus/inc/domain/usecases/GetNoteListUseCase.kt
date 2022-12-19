package com.octopus.inc.domain.usecases

import com.octopus.inc.domain.models.NoteModel
import com.octopus.inc.domain.repository.NoteRepository

class GetNoteListUseCase(val noteRepository: NoteRepository) {
    fun execute(): List<NoteModel> {
        return noteRepository.getNoteList()
    }
}