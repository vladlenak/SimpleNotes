package akhtemov.vladlen.simplenotes.presentation.notelist

import akhtemov.vladlen.simplenotes.presentation.model.NoteView

data class NoteListState(
    val noteModelList: List<NoteView>
)