package akhtemov.vladlen.simplenotes.viewmodel

import akhtemov.vladlen.simplenotes.db.Note
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditNoteViewModel : ViewModel() {
    val note = MutableLiveData<Note>()
    val isDueDateEmpty = MutableLiveData<Boolean>()
}