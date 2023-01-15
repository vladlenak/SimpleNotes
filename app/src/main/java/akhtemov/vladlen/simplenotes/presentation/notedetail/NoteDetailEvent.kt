package akhtemov.vladlen.simplenotes.presentation.notedetail

import com.octopus.inc.domain.models.NoteModel

interface NoteDetailEvent

class SetNotesEvent(val noteId: String) : NoteDetailEvent

class UpdateNoteEvent(val noteModel: NoteModel) : NoteDetailEvent

class DeleteNoteEvent() : NoteDetailEvent