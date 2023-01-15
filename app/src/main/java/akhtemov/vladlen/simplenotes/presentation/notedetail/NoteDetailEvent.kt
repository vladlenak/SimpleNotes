package akhtemov.vladlen.simplenotes.presentation.notedetail

import com.octopus.inc.domain.models.Note

interface NoteDetailEvent

class SetNoteEvent(val noteId: String) : NoteDetailEvent

class UpdateNoteEvent(val noteModel: Note) : NoteDetailEvent

class DeleteNoteEvent() : NoteDetailEvent