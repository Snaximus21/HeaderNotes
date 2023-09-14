package com.rg.headernotes.ui.employers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rg.headernotes.repository.EmployersRepository
import com.rg.headernotes.util.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class EmployersViewModel @Inject constructor(private val repository: EmployersRepository) : ViewModel() {

    private val _allEmployers = MutableLiveData<UiState<List<EmployerModel>>>()
    val allEmployers: LiveData<UiState<List<EmployerModel>>> get() = _allEmployers

    fun getAllEmployers(employerName: String) {
        _allEmployers.value = UiState.Loading
        repository.getAllEmployers(employerName) {
            _allEmployers.value = it
        }
    }

    private val _update = MutableLiveData<UiState<String>>()
    val update: LiveData<UiState<String>> get() = _update
    fun updateEmployer(employer: EmployerModel, employerName: String) {
        _update.value = UiState.Loading
        viewModelScope.launch {
            repository.updateEmployer(employer, employerName) {
                _update.value = it
            }
        }
    }

    private val _delete = MutableLiveData<UiState<String>>()
    val delete: LiveData<UiState<String>> get() = _delete
    fun deleteEmployer(employerID: String, employerName: String) {
        _delete.value = UiState.Loading
        viewModelScope.launch {
            repository.deleteEmployer(employerID, employerName) {
                _delete.value = it
            }
        }
    }
}