package akhtemov.vladlen.simplenotes.viewmodel

import akhtemov.vladlen.simplenotes.db.Note
import akhtemov.vladlen.simplenotes.db.NoteRepository
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

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

class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}