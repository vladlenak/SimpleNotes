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
class NoteViewModel @Inject constructor(
    private val saveNoteUseCase: SaveNoteUseCase,
    private val getNoteListUseCase: GetNoteListUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val _notes = MutableLiveData<List<NoteModel>>()
    val notes: LiveData<List<NoteModel>> = _notes

    fun insertNote(note: NoteModel) = viewModelScope.launch {
        saveNoteUseCase.execute(note)
        setNotes()
    }

    fun deleteNoteByPosition(position: Int) = viewModelScope.launch {
        val note = _notes.value?.get(position)

        if (note != null) {
            deleteNoteUseCase.execute(note)
        }

        setNotes()
    }

    fun setNotes() = viewModelScope.launch {
        _notes.value = getNoteListUseCase.execute()
    }
}