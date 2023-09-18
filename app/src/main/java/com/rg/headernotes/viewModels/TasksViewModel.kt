package com.rg.headernotes.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rg.headernotes.ui.tasks.TaskModel
import com.rg.headernotes.util.UiState
import com.rg.headertasks.repository.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(private val repository: TasksRepository) : ViewModel() {

    private val _allTasks = MutableLiveData<UiState<List<TaskModel>>>()
    val allTasks: LiveData<UiState<List<TaskModel>>> get() = _allTasks

    fun getAllTasks() {
        _allTasks.value = UiState.Loading
        repository.getAllTasks {
            _allTasks.value = it
        }
    }

    private val _update = MutableLiveData<UiState<String>>()
    val update: LiveData<UiState<String>> get() = _update
    fun updateTask(note: TaskModel) {
        _update.value = UiState.Loading
        viewModelScope.launch {
            repository.updateTask(note) {
                _update.value = it
            }
        }
    }

    private val _delete = MutableLiveData<UiState<String>>()
    val delete: LiveData<UiState<String>> get() = _delete
    fun deleteTask(note: TaskModel) {
        _delete.value = UiState.Loading
        viewModelScope.launch {
            repository.deleteTask(note) {
                _delete.value = it
            }
        }
    }

    private val _newTask = MutableLiveData<UiState<TaskModel>>()
    val newTask: LiveData<UiState<TaskModel>> get() = _newTask
    fun newTask(note: TaskModel) {
        _newTask.value = UiState.Loading
        repository.addTask(note) {
            _newTask.value = it
        }
    }
}