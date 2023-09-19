package com.rg.headernotes.dao

import com.rg.headernotes.models.EmployerModel
import com.rg.headernotes.models.NoteModel
import com.rg.headernotes.models.UserModel
import com.rg.headernotes.ui.tasks.TaskModel
import com.rg.headernotes.util.UiState

interface EmployersDao {
    fun getAllEmployers(result: (UiState<List<EmployerModel>>) -> Unit)
    fun getEmployer(employerID: String, employerName: String, result: (UiState<EmployerModel>) -> Unit)
    fun addEmployer(employer: EmployerModel, employerName: String, result: (UiState<EmployerModel>) -> Unit)
    fun updateEmployer(employer: EmployerModel, result: (UiState<String>) -> Unit)
    fun deleteEmployer(employerName: String, result: (UiState<String>) -> Unit)
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