package com.rg.headernotes.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rg.headernotes.repository.UserRepository
import com.rg.headernotes.models.UserModel
import com.rg.headernotes.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: UserRepository): ViewModel() {

    private val _user = MutableLiveData<UiState<UserModel>>()
    val user: LiveData<UiState<UserModel>> get() = _user
    fun getUser() {
        _user.value = UiState.Loading
        repository.getUser {
            _user.value = it
        }
    }
}