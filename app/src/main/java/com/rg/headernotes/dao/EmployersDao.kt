package com.rg.headernotes.dao

import com.rg.headernotes.models.EmployerModel
import com.rg.headernotes.models.NoteModel
import com.rg.headernotes.models.TaskModel
import com.rg.headernotes.util.UiState

interface EmployersDao {
    fun getAllEmployers(result: (UiState<List<EmployerModel>>) -> Unit)
    fun getEmployer(employer: EmployerModel, result: (UiState<EmployerModel>) -> Unit)
    fun addEmployer(employer: EmployerModel,  result: (UiState<EmployerModel>) -> Unit)
    fun updateEmployer(employer: EmployerModel, result: (UiState<String>) -> Unit)
    fun deleteEmployer(employer: EmployerModel, result: (UiState<String>) -> Unit)
    fun getAllNotes(employer: EmployerModel, result: (UiState<List<NoteModel>>) -> Unit)
    fun getNote(employer: EmployerModel, note: NoteModel, result: (UiState<NoteModel>) -> Unit)
    fun addNote(employer: EmployerModel, note: NoteModel, result: (UiState<NoteModel>) -> Unit)
    fun updateNote(employer: EmployerModel, note: NoteModel, result: (UiState<String>) -> Unit)
    fun deleteNote(employer: EmployerModel, note: NoteModel, result: (UiState<String>) -> Unit)
    fun getAllTasks(employer: EmployerModel, result: (UiState<List<TaskModel>>) -> Unit)
    fun getTask(employer: EmployerModel, task: TaskModel, result: (UiState<TaskModel>) -> Unit)
    fun addTask(employer: EmployerModel, task: TaskModel, result: (UiState<TaskModel>) -> Unit)
    fun updateTask(employer: EmployerModel, task: TaskModel, result: (UiState<String>) -> Unit)
    fun deleteTask(employer: EmployerModel, task: TaskModel, result: (UiState<String>) -> Unit)
}