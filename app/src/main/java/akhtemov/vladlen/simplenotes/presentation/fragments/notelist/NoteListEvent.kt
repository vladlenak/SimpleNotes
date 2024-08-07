package akhtemov.vladlen.simplenotes.presentation.fragments.notelist

import akhtemov.vladlen.simplenotes.presentation.model.NoteView

interface NoteListEvent

class InsertNoteEvent(val noteModel: NoteView) : NoteListEvent

class DeleteNoteEvent(val noteModel: NoteView) : NoteListEvent

class SetNotesEvent : NoteListEvent