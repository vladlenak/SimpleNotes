package akhtemov.vladlen.simplenotes.viewmodel

import akhtemov.vladlen.simplenotes.db.Note
import akhtemov.vladlen.simplenotes.db.NoteRepository
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {

    val notes = MutableLiveData<List<Note>>()

    fun insert(note: Note) = viewModelScope.launch {
        repository.insert(note)
        setNotes()
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        repository.deleteNote(note)
        setNotes()
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        repository.updateNote(note)
        setNotes()
    }

    fun setNotes() = viewModelScope.launch {
        notes.value = repository.getNotes()
    }
}