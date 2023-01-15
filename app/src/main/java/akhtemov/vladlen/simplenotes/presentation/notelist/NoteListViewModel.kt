package akhtemov.vladlen.simplenotes.presentation.notelist

import androidx.lifecycle.*
import com.octopus.inc.domain.models.NoteModel
import com.octopus.inc.domain.usecases.DeleteNoteUseCase
import com.octopus.inc.domain.usecases.GetNoteListUseCase
import com.octopus.inc.domain.usecases.SaveNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val saveNoteUseCase: SaveNoteUseCase,
    private val getNoteListUseCase: GetNoteListUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val _noteListState = MutableLiveData<NoteListState>()
    val noteListState: LiveData<NoteListState> = _noteListState

    fun send(event: NoteListEvent) {
        when (event) {
            is InsertNoteEvent -> {
                insertNote(event.noteModel)
            }
            is DeleteNoteEvent -> {
                deleteNoteByPosition(event.noteModel)
            }
            is SetNotesEvent -> {
                setNotes()
            }
        }
    }

    private fun  insertNote(note: NoteModel) = viewModelScope.launch {
        saveNoteUseCase.execute(note)
        setNotes()
    }

    private fun deleteNoteByPosition(note: NoteModel) = viewModelScope.launch {
        deleteNoteUseCase.execute(note)
        setNotes()
    }

    private fun setNotes() = viewModelScope.launch {
        _noteListState.value = NoteListState(getNoteListUseCase.execute())
    }
}