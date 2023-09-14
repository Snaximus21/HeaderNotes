package com.rg.headernotes.ui.addUser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rg.headernotes.repository.UserRepository
import com.rg.headernotes.ui.employers.EmployerModel
import com.rg.headernotes.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(private val repository: UserRepository): ViewModel() {

    private val _addNewUser = MutableLiveData<UiState<String>>()
    val addNewUser: LiveData<UiState<String>> get() = _addNewUser
    fun addUser(user: UserModel) {
        _addNewUser.value = UiState.Loading
        repository.addUser(user) {
            _addNewUser.value = it
        }
    }
}