package com.rg.headertasks.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rg.headernotes.models.TaskModel
import com.rg.headernotes.util.FireStoreTables
import com.rg.headernotes.util.Strings
import com.rg.headernotes.util.UiState
import com.rg.headertasks.dao.TasksDao
import javax.inject.Inject

class TasksRepository @Inject constructor(private val database: FirebaseFirestore) : TasksDao {
    override fun getAllTasks(result: (UiState<List<TaskModel>>) -> Unit) {
        val tasks = mutableListOf<TaskModel>()
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.TASKS)
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        tasks += TaskModel(
                            document.data["id"].toString(),
                            document.data["taskName"].toString(),
                            document.data["taskNote"].toString(),
                            document.data["taskDate"].toString()
                        )
                    }
                    result.invoke(UiState.Success(tasks))
                }
                .addOnFailureListener {
                    result.invoke(UiState.Failure(Strings.ERROR))
                }
        }
    }

    override fun getTask(task: TaskModel, result: (UiState<TaskModel>) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.TASKS)
                .document(task.id)
                .get()
                .addOnSuccessListener {
                    result.invoke(
                        UiState.Success(
                            it.data?.let { data ->
                                TaskModel(
                                    data["id"].toString(),
                                    data["taskName"].toString(),
                                    data["taskNote"].toString(),
                                    data["taskDate"].toString()
                                )
                            }
                                ?: run {
                                    TaskModel()
                                }
                        )
                    )
                }
                .addOnFailureListener {
                    result.invoke(UiState.Failure(Strings.ERROR))
                }
        }
    }

    override fun addTask(task: TaskModel, result: (UiState<TaskModel>) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.TASKS)
                .document(task.id)
                .set(task)
                .addOnSuccessListener {
                    result.invoke(UiState.Success(task))
                }
                .addOnFailureListener {
                    result.invoke(UiState.Failure(Strings.ERROR))
                }
        }
    }

    override fun updateTask(task: TaskModel, result: (UiState<String>) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.TASKS)
                .document(task.id)
                .set(task)
                .addOnSuccessListener {
                    result.invoke(UiState.Success(Strings.UPDATED))
                }
                .addOnFailureListener {
                    result.invoke(UiState.Failure(Strings.ERROR))
                }
        }
    }

    override fun deleteTask(task: TaskModel, result: (UiState<String>) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.TASKS)
                .document(task.id)
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