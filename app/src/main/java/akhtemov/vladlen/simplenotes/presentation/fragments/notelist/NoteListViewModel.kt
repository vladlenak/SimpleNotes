package akhtemov.vladlen.simplenotes.presentation.fragments.notelist

import akhtemov.vladlen.simplenotes.presentation.mapper.NoteMapper
import akhtemov.vladlen.simplenotes.presentation.model.NoteView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octopus.inc.domain.usecases.DeleteNoteUseCase
import com.octopus.inc.domain.usecases.GetNoteListUseCase
import com.octopus.inc.domain.usecases.SaveNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val saveNoteUseCase: SaveNoteUseCase,
    private val getNoteListUseCase: GetNoteListUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val noteMapper: NoteMapper
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

    private fun insertNote(note: NoteView) = viewModelScope.launch(Dispatchers.IO) {
        saveNoteUseCase.execute(noteMapper.mapFromView(note))
        setNotes()
    }

    private fun deleteNoteByPosition(note: NoteView) = viewModelScope.launch(Dispatchers.IO) {
        deleteNoteUseCase.execute(noteMapper.mapFromView(note))
        setNotes()
    }

    private fun setNotes() = viewModelScope.launch(Dispatchers.IO) {
        _noteListState.postValue(NoteListState(getNoteListUseCase.execute().map { noteMapper.mapToView(it) }))
    }
}