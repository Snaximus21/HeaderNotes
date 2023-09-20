package com.rg.headernotes.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rg.headernotes.repository.EmployersRepository
import com.rg.headernotes.models.EmployerModel
import com.rg.headernotes.models.NoteModel
import com.rg.headernotes.models.TaskModel
import com.rg.headernotes.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployersViewModel @Inject constructor(private val repository: EmployersRepository) : ViewModel() {

    private val _allEmployers = MutableLiveData<UiState<List<EmployerModel>>>()
    val allEmployers: LiveData<UiState<List<EmployerModel>>> get() = _allEmployers

    fun getAllEmployers() {
        _allEmployers.value = UiState.Loading
        repository.getAllEmployers() {
            _allEmployers.value = it
        }
    }

    private val _update = MutableLiveData<UiState<String>>()
    val update: LiveData<UiState<String>> get() = _update
    fun updateEmployer(employer: EmployerModel) {
        _update.value = UiState.Loading
        viewModelScope.launch {
            repository.updateEmployer(employer) {
                _update.value = it
            }
        }
    }

    private val _delete = MutableLiveData<UiState<String>>()
    val delete: LiveData<UiState<String>> get() = _delete
    fun deleteEmployer(employer: EmployerModel) {
        _delete.value = UiState.Loading
        viewModelScope.launch {
            repository.deleteEmployer(employer) {
                _delete.value = it
            }
        }
    }

    private val _newEmployer = MutableLiveData<UiState<EmployerModel>>()
    val newEmployer: LiveData<UiState<EmployerModel>> get() = _newEmployer
    fun newEmployer(employer: EmployerModel) {
        _newEmployer.value = UiState.Loading
        repository.addEmployer(employer) {
            _newEmployer.value = it
        }
    }

    private val _allNotes = MutableLiveData<UiState<List<NoteModel>>>()
    val allNotes: LiveData<UiState<List<NoteModel>>> get() = _allNotes

    fun getAllNotes(employer: EmployerModel) {
        _allNotes.value = UiState.Loading
        repository.getAllNotes(employer) {
            _allNotes.value = it
        }
    }

    private val _updateNote = MutableLiveData<UiState<String>>()
    val updateNote: LiveData<UiState<String>> get() = _updateNote
    fun updateNote(employer: EmployerModel, note: NoteModel) {
        _updateNote.value = UiState.Loading
        viewModelScope.launch {
            repository.updateNote(employer, note) {
                _updateNote.value = it
            }
        }
    }

    private val _deleteNote = MutableLiveData<UiState<String>>()
    val deleteNote: LiveData<UiState<String>> get() = _deleteNote
    fun deleteNote(employer: EmployerModel, note: NoteModel) {
        _deleteNote.value = UiState.Loading
        viewModelScope.launch {
            repository.deleteNote(employer, note) {
                _deleteNote.value = it
            }
        }
    }

    private val _newNote = MutableLiveData<UiState<NoteModel>>()
    val newNote: LiveData<UiState<NoteModel>> get() = _newNote
    fun newNote(employer: EmployerModel,note: NoteModel) {
        _newNote.value = UiState.Loading
        repository.addNote(employer, note) {
            _newNote.value = it
        }
    }

    private val _allTasks = MutableLiveData<UiState<List<TaskModel>>>()
    val allTasks: LiveData<UiState<List<TaskModel>>> get() = _allTasks

    fun getAllTasks(employer: EmployerModel) {
        _allTasks.value = UiState.Loading
        repository.getAllTasks(employer) {
            _allTasks.value = it
        }
    }

    private val _updateTask = MutableLiveData<UiState<String>>()
    val updateTask: LiveData<UiState<String>> get() = _updateTask
    fun updateTask(employer: EmployerModel, note: TaskModel) {
        _updateTask.value = UiState.Loading
        viewModelScope.launch {
            repository.updateTask(employer, note) {
                _updateTask.value = it
            }
        }
    }

    private val _deleteTask = MutableLiveData<UiState<String>>()
    val deleteTask: LiveData<UiState<String>> get() = _deleteTask
    fun deleteTask(employer: EmployerModel, note: TaskModel) {
        _deleteTask.value = UiState.Loading
        viewModelScope.launch {
            repository.deleteTask(employer, note) {
                _deleteTask.value = it
            }
        }
    }

    private val _newTask = MutableLiveData<UiState<TaskModel>>()
    val newTask: LiveData<UiState<TaskModel>> get() = _newTask
    fun newTask(employer: EmployerModel, note: TaskModel) {
        _newTask.value = UiState.Loading
        repository.addTask(employer, note) {
            _newTask.value = it
        }
    }
}