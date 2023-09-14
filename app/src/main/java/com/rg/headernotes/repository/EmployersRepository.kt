package com.rg.headernotes.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rg.headernotes.dao.EmployersDao
import com.rg.headernotes.ui.employers.EmployerModel
import com.rg.headernotes.util.FireStoreTables
import com.rg.headernotes.util.Strings
import com.rg.headernotes.util.UiState
import javax.inject.Inject

class EmployersRepository @Inject constructor(private val database: FirebaseFirestore) :
    EmployersDao {
    override fun getAllEmployers(
        employerName: String,
        result: (UiState<List<EmployerModel>>) -> Unit
    ) {
        val employers = mutableListOf<EmployerModel>()
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employerName)
        }
    }

    override fun getEmployer(
        employerID: String,
        employerName: String,
        result: (UiState<EmployerModel>) -> Unit
    ) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employerName)
                .get()
                .addOnSuccessListener {
                    result.invoke(UiState.Success(EmployerModel()))
                }
                .addOnFailureListener {
                    result.invoke(UiState.Failure(Strings.ERROR))
                }
        }
    }

    override suspend fun addEmployer(
        employer: EmployerModel,
        employerName: String,
        result: (UiState<String>) -> Unit
    ) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employerName)
                .set(employer)
                .addOnSuccessListener {
                    result.invoke(UiState.Success(Strings.ADDED))
                }
                .addOnFailureListener {
                    result.invoke(UiState.Failure(Strings.ERROR))
                }
        }
    }

    override suspend fun updateEmployer(
        employer: EmployerModel,
        employerName: String,
        result: (UiState<String>) -> Unit
    ) {
        FirebaseAuth.getInstance().currentUser?.let { user ->

        }
    }

    //TODO Нужно удалять данные из коллекции
    override suspend fun deleteEmployer(
        employerID: String,
        employerName: String,
        result: (UiState<String>) -> Unit
    ) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employerName)
                .delete()
                .addOnSuccessListener {
                    result.invoke(UiState.Success(Strings.DELETED))
                }
                .addOnFailureListener {
                    result.invoke(UiState.Failure(Strings.ERROR))
                }
        }
    }
}