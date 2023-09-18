package com.rg.headernotes.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rg.headernotes.repository.EmployersRepository
import com.rg.headernotes.models.EmployerModel
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
    fun deleteEmployer(employerName: String) {
        _delete.value = UiState.Loading
        viewModelScope.launch {
            repository.deleteEmployer(employerName) {
                _delete.value = it
            }
        }
    }

    private val _newEmployer = MutableLiveData<UiState<EmployerModel>>()
    val newEmployer: LiveData<UiState<EmployerModel>> get() = _newEmployer
    fun newEmployer(employer: EmployerModel) {
        _newEmployer.value = UiState.Loading
        repository.addEmployer(employer, employer.fullName) {
            _newEmployer.value = it
        }
    }
}