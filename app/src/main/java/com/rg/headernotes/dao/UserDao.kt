package com.rg.headernotes.dao

import com.rg.headernotes.models.UserModel
import com.rg.headernotes.util.UiState

/**
 * Замечания справедливы ко всем *Dao:
 *
 * 1. У тебя тут в качестве последнего параметра UiState, но UiState и DAO довольно далеки друг от друга,
 * DAO сущность дата слоя, а UIState уже исполючительно про UI, правильнее конечно тут с какими то другими
 * сущностями работать
 *
 * 2. Не совсем понял почему у тебя интерфейс DAO, а его имплементация это уже репозиторий
 */
interface UserDao {
    fun addUser(userModel: UserModel, result: (UiState<String>) -> Unit)
    fun deleteUser(result: (UiState<String>) -> Unit)
    fun getUser(result: (UiState<UserModel>) -> Unit)
}