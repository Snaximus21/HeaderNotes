package com.rg.headernotes.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rg.headernotes.repository.NotesRepository
import com.rg.headernotes.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val repository: NotesRepository) : ViewModel() {

    private val _allNotes = MutableLiveData<UiState<List<NoteModel>>>()
    val allNotes: LiveData<UiState<List<NoteModel>>> get() = _allNotes

    fun getAllNotes() {
        _allNotes.value = UiState.Loading
        repository.getAllNotes {
            _allNotes.value = it
        }
    }

    private val _update = MutableLiveData<UiState<String>>()
    val update: LiveData<UiState<String>> get() = _update
    fun updateNote(note: NoteModel) {
        _update.value = UiState.Loading
        viewModelScope.launch {
            repository.updateNote(note) {
                _update.value = it
            }
        }
    }

    private val _delete = MutableLiveData<UiState<String>>()
    val delete: LiveData<UiState<String>> get() = _delete
    fun deleteNote(note: NoteModel) {
        _delete.value = UiState.Loading
        viewModelScope.launch {
            repository.deleteNote(note) {
                _delete.value = it
            }
        }
    }

    private val _newNote = MutableLiveData<UiState<NoteModel>>()
    val newNote: LiveData<UiState<NoteModel>> get() = _newNote
    fun newNote(note: NoteModel) {
        _newNote.value = UiState.Loading
        repository.addNote(note) {
            _newNote.value = it
        }
    }
}