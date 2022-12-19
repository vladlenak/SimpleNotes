package akhtemov.vladlen.simplenotes.presentation.notelist

import androidx.lifecycle.*
import com.octopus.inc.domain.models.NoteModel
import com.octopus.inc.domain.usecases.DeleteNoteUseCase
import com.octopus.inc.domain.usecases.GetNoteListUseCase
import com.octopus.inc.domain.usecases.SaveNoteUseCase
import com.octopus.inc.domain.usecases.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val saveNoteUseCase: SaveNoteUseCase,
    private val getNoteListUseCase: GetNoteListUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase
) : ViewModel() {

    val notes = MutableLiveData<List<NoteModel>>()

    fun insert(note: NoteModel) = viewModelScope.launch {
        saveNoteUseCase.execute(note)
        setNotes()
    }

    fun deleteNote(note: NoteModel) = viewModelScope.launch {
        deleteNoteUseCase.execute(note)
        setNotes()
    }

    fun updateNote(note: NoteModel) = viewModelScope.launch {
        updateNoteUseCase.execute(note)
        setNotes()
    }

    fun setNotes() = viewModelScope.launch {
        notes.value = getNoteListUseCase.execute()
    }
}