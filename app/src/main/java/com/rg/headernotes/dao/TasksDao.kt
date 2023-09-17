package com.rg.headertasks.dao

import com.rg.headernotes.ui.tasks.TaskModel
import com.rg.headernotes.util.UiState

interface TasksDao {
    fun getAllTasks(result: (UiState<List<TaskModel>>) -> Unit)
    fun getTask(task: TaskModel, result: (UiState<TaskModel>) -> Unit)
    fun addTask(task: TaskModel, result: (UiState<TaskModel>) -> Unit)
    fun updateTask(task: TaskModel, result: (UiState<String>) -> Unit)
    fun deleteTask(task: TaskModel, result: (UiState<String>) -> Unit)
}