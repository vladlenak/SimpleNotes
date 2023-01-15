package akhtemov.vladlen.simplenotes.presentation.notelist

import com.octopus.inc.domain.models.NoteModel

interface NoteListEvent

class InsertNoteEvent(val noteModel: NoteModel) : NoteListEvent

class DeleteNoteEvent(val noteModel: NoteModel) : NoteListEvent

class SetNotesEvent : NoteListEvent