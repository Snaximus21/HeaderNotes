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
                .addOnSuccessListener { employers ->
                    for (employer in employers) {
                        dataBase
                            .collection(FireStoreTables.USER)
                            .document(user.uid)
                            .collection(FireStoreTables.EMPLOYERS)
                            .document(employer.data["fullName"].toString())
                            .delete()
                    }
                }
                .addOnFailureListener {

                }

            dataBase
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.NOTES)
                .get()
                .addOnSuccessListener { employers ->
                    for (employer in employers) {
                        dataBase
                            .collection(FireStoreTables.USER)
                            .document(user.uid)
                            .collection(FireStoreTables.NOTES)
                            .document(employer.data["noteTitle"].toString())
                            .delete()
                    }
                }
                .addOnFailureListener {

                }

            dataBase
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .delete()
                .addOnSuccessListener {
                    result.invoke(UiState.Success(Strings.DELETED))
                }.addOnFailureListener {
                    result.invoke(UiState.Failure(Strings.ERROR))
                }
        }
    }

    override fun getUser(result: (UiState<UserModel>) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            dataBase
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .get()
                .addOnSuccessListener {
                    result.invoke(
                        UiState.Success(
                            UserModel(
                                name = it.get("name").toString(),
                                subDivision = it.get("subDivision").toString()
                            )
                        )
                    )
                }
                .addOnFailureListener {
                    result.invoke(UiState.Failure(Strings.ERROR))
                }
        }
    }
}