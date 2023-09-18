package com.rg.headernotes.dao

import com.rg.headernotes.models.UserModel
import com.rg.headernotes.util.UiState

interface UserDao {
    fun addUser(userModel: UserModel, result: (UiState<String>) -> Unit)
    fun deleteUser(result: (UiState<String>) -> Unit)
    fun getUser(result: (UiState<UserModel>) -> Unit)
}