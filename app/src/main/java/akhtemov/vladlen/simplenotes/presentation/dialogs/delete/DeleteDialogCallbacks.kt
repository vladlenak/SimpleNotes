package akhtemov.vladlen.simplenotes.presentation.dialogs.delete

import akhtemov.vladlen.simplenotes.presentation.model.NoteView

interface DeleteDialogCallbacks {
    fun onClickYesOnDeleteDialog(note: NoteView)
    fun onClickNoOnDeleteDialog()
}