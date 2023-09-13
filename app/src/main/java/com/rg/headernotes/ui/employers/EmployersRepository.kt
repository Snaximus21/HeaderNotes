package com.rg.headernotes.ui.employers

import com.google.firebase.firestore.FirebaseFirestore
import com.rg.headernotes.util.UiState
import javax.inject.Inject

class EmployersRepository @Inject constructor(private val database: FirebaseFirestore): EmployersDao {
    override fun getAllEmployers(
        employerName: String,
        result: (UiState<List<EmployerModel>>) -> Unit
    ) {

    }

    override fun getEmployer(
        employerID: String,
        employerName: String,
        result: (UiState<EmployerModel>) -> Unit
    ) {

    }

    override suspend fun addEmployer(
        employer: EmployerModel,
        employerName: String,
        result: (UiState<String>) -> Unit
    ) {

    }

    override suspend fun updateEmployer(
        employer: EmployerModel,
        employerName: String,
        result: (UiState<String>) -> Unit
    ) {

    }

    override suspend fun deleteEmployer(
        employerID: String,
        employerName: String,
        result: (UiState<String>) -> Unit
    ) {

    }
}