package akhtemov.vladlen.simplenotes.presentation.notelist

import com.octopus.inc.domain.models.NoteModel

data class NoteListState(
    val noteModelList: List<NoteModel>
)