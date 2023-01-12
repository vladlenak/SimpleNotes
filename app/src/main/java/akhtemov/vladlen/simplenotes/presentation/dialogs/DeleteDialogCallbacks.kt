package akhtemov.vladlen.simplenotes.presentation.dialogs

import com.octopus.inc.domain.models.NoteModel

interface DeleteDialogCallbacks {
    fun onClickYesOnDeleteDialog(note: NoteModel)
    fun onClickNoOnDeleteDialog()
}