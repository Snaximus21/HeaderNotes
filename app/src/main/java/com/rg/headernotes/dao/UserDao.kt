package com.rg.headernotes.dao

import com.rg.headernotes.ui.addUser.UserModel
import com.rg.headernotes.util.UiState

interface UserDao {
    fun addUser(userModel: UserModel, result: (UiState<String>) -> Unit)
    fun deleteUser(result: (UiState<String>) -> Unit)
}