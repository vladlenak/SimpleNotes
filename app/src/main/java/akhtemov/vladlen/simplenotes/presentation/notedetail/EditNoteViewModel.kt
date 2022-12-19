package akhtemov.vladlen.simplenotes.presentation.notedetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.octopus.inc.domain.models.NoteModel

class EditNoteViewModel : ViewModel() {
    val note = MutableLiveData<NoteModel>()
    val isDueDateEmpty = MutableLiveData<Boolean>()
}