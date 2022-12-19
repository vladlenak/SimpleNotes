package akhtemov.vladlen.simplenotes.viewmodel

import akhtemov.vladlen.simplenotes.db.NoteRepositoryImpl
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
//        repository.insertNote(note)
        saveNoteUseCase.execute(note)
        setNotes()
    }

    fun deleteNote(note: NoteModel) = viewModelScope.launch {
//        repository.deleteNote(note)
        deleteNoteUseCase.execute(note)
        setNotes()
    }

    fun updateNote(note: NoteModel) = viewModelScope.launch {
//        repository.updateNote(note)
        updateNoteUseCase.execute(note)
        setNotes()
    }

    fun setNotes() = viewModelScope.launch {
//        notes.value = repository.getNotes()
        notes.value = getNoteListUseCase.execute()
    }
}