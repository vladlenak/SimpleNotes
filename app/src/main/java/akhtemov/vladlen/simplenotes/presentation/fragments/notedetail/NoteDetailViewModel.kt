package akhtemov.vladlen.simplenotes.presentation.fragments.notedetail

import akhtemov.vladlen.simplenotes.presentation.mapper.NoteMapper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octopus.inc.domain.models.Note
import com.octopus.inc.domain.usecases.DeleteNoteUseCase
import com.octopus.inc.domain.usecases.GetNoteUseCase
import com.octopus.inc.domain.usecases.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val getNoteUseCase: GetNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val noteMapper: NoteMapper
) : ViewModel() {

    private val _noteDetailState = MutableLiveData<NoteDetailState>()
    val noteDetailState: LiveData<NoteDetailState> = _noteDetailState

    fun send(event: NoteDetailEvent) {
        when(event) {
            is SetNoteEvent -> {
                setNote(event.noteId)
            }
            is UpdateNoteEvent -> {
                updateNote(event.noteModel)
            }
            is DeleteNoteEvent -> {
                deleteNote()
            }
        }
    }

    private fun setNote(noteId: String) = viewModelScope.launch(Dispatchers.IO) {
        _noteDetailState.postValue(NoteDetailState(noteMapper.mapToView(getNoteUseCase.execute(noteId))))
    }

    private fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        updateNoteUseCase.execute(note)
    }

    private fun deleteNote() = viewModelScope.launch(Dispatchers.IO) {
        noteDetailState.value?.noteModel?.let { note -> deleteNoteUseCase.execute(noteMapper.mapFromView(note)) }
    }
}