package akhtemov.vladlen.simplenotes.presentation.notedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octopus.inc.domain.models.NoteModel
import com.octopus.inc.domain.usecases.GetNoteUseCase
import com.octopus.inc.domain.usecases.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val getNoteUseCase: GetNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase
) : ViewModel() {

    private val _note = MutableLiveData<NoteModel>()
    val note: LiveData<NoteModel> = _note

    fun getNote(noteId: String) = viewModelScope.launch {
        _note.value = getNoteUseCase.execute(noteId)
    }

    fun updateNote(note: NoteModel) = viewModelScope.launch {
        updateNoteUseCase.execute(note)
    }
}