package com.rg.headernotes.dao

import com.rg.headernotes.ui.employers.EmployerModel
import com.rg.headernotes.util.UiState

interface EmployersDao {
    fun getAllEmployers(employerName: String, result: (UiState<List<EmployerModel>>) -> Unit)
    fun getEmployer(employerID: String, employerName: String, result: (UiState<EmployerModel>) -> Unit)
    suspend fun addEmployer(employer: EmployerModel, employerName: String, result: (UiState<String>) -> Unit)
    suspend fun updateEmployer(employer: EmployerModel, employerName: String, result: (UiState<String>) -> Unit)
    suspend fun deleteEmployer(employerID: String, employerName: String, result: (UiState<String>) -> Unit)
}