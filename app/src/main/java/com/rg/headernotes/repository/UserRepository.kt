package com.rg.headernotes.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rg.headernotes.R
import com.rg.headernotes.dao.UserDao
import com.rg.headernotes.ui.addUser.UserModel
import com.rg.headernotes.ui.employers.EmployerModel
import com.rg.headernotes.util.FireStoreTables
import com.rg.headernotes.util.Strings
import com.rg.headernotes.util.UiState
import java.util.UUID
import javax.inject.Inject

class UserRepository @Inject constructor(private val dataBase: FirebaseFirestore) : UserDao {
    override fun addUser(userModel: UserModel, result: (UiState<String>) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            dataBase
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .set(userModel)
                .addOnSuccessListener {
                    result.invoke(UiState.Success(Strings.CREATED))
                }
                .addOnFailureListener {
                    result.invoke(UiState.Failure(Strings.ERROR))
                }
        }
    }

    override fun deleteUser(result: (UiState<String>) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            dataBase
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .get()
                .addOnSuccessListener { query ->
                    query.forEach { document ->

                    }
                }
                .addOnFailureListener {

                }

            dataBase
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .delete()
                .addOnSuccessListener {
                    result.invoke(UiState.Failure(Strings.DELETED))
                }.addOnFailureListener {
                    result.invoke(UiState.Failure(Strings.ERROR))
                }
        }
    }
}