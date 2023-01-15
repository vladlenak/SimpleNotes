package akhtemov.vladlen.simplenotes.presentation.deletedialog

import akhtemov.vladlen.simplenotes.presentation.model.NoteView

interface DeleteDialogCallbacks {
    fun onClickYesOnDeleteDialog(note: NoteView)
    fun onClickNoOnDeleteDialog()
}