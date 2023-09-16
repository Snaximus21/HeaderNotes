package com.rg.headernotes.ui.addUser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rg.headernotes.repository.UserRepository
import com.rg.headernotes.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository): ViewModel() {

    private val _addNewUser = MutableLiveData<UiState<String>>()
    val addNewUser: LiveData<UiState<String>> get() = _addNewUser
    fun addUser(user: UserModel) {
        _addNewUser.value = UiState.Loading
        repository.addUser(user) {
            _addNewUser.value = it
        }
    }

    private val _deleteUser = MutableLiveData<UiState<String>>()
    val deleteUser: LiveData<UiState<String>> get() = _deleteUser
    fun deleteUser() {
        _deleteUser.value = UiState.Loading
        repository.deleteUser {
            _deleteUser.value = it
        }
    }
}