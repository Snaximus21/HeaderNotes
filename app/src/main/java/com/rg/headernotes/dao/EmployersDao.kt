package com.rg.headernotes.dao

import com.rg.headernotes.models.EmployerModel
import com.rg.headernotes.util.UiState

interface EmployersDao {
    fun getAllEmployers(result: (UiState<List<EmployerModel>>) -> Unit)
    fun getEmployer(employerID: String, employerName: String, result: (UiState<EmployerModel>) -> Unit)
    fun addEmployer(employer: EmployerModel, employerName: String, result: (UiState<EmployerModel>) -> Unit)
    fun updateEmployer(employer: EmployerModel, result: (UiState<String>) -> Unit)
    fun deleteEmployer(employerName: String, result: (UiState<String>) -> Unit)
}