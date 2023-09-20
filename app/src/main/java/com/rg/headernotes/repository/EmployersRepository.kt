package com.rg.headernotes.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rg.headernotes.dao.EmployersDao
import com.rg.headernotes.models.EmployerModel
import com.rg.headernotes.models.NoteModel
import com.rg.headernotes.models.TaskModel
import com.rg.headernotes.util.FireStoreTables
import com.rg.headernotes.util.Strings
import com.rg.headernotes.util.UiState
import javax.inject.Inject

class EmployersRepository @Inject constructor(private val database: FirebaseFirestore) :
    EmployersDao {
    override fun getAllEmployers(
        result: (UiState<List<EmployerModel>>) -> Unit
    ) {
        val employers = mutableListOf<EmployerModel>()
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        employers += EmployerModel(
                            id = document.data["id"].toString(),
                            fullName = document.data["fullName"].toString(),
                            job = document.data["job"].toString(),
                            age = document.data["age"].toString(),
                            notesCount = document.data["notesCount"].toString(),
                            tasksCount = document.data["tasksCount"].toString(),
                        )
                    }
                    result.invoke(UiState.Success(employers))
                }
                .addOnFailureListener {
                    result.invoke(UiState.Failure(Strings.ERROR))
                }
        }
    }

    override fun getEmployer(
        employer: EmployerModel,
        result: (UiState<EmployerModel>) -> Unit
    ) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employer.id)
                .get()
                .addOnSuccessListener {
                    result.invoke(UiState.Success(EmployerModel()))
                }
                .addOnFailureListener {
                    result.invoke(UiState.Failure(Strings.ERROR))
                }
        }
    }

    override fun addEmployer(
        employer: EmployerModel,
        result: (UiState<EmployerModel>) -> Unit
    ) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employer.id)
                .set(employer)
                .addOnSuccessListener {
                    result.invoke(UiState.Success(employer))
                }
                .addOnFailureListener {
                    result.invoke(UiState.Failure(Strings.ERROR))
                }
        }
    }

    override fun updateEmployer(
        employer: EmployerModel,
        result: (UiState<String>) -> Unit
    ) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            FirebaseAuth.getInstance().currentUser?.let { user ->
                database
                    .collection(FireStoreTables.USER)
                    .document(user.uid)
                    .collection(FireStoreTables.EMPLOYERS)
                    .document(employer.id)
                    .set(employer)
                    .addOnSuccessListener {
                        result.invoke(UiState.Success(Strings.UPDATED))
                    }
                    .addOnFailureListener {
                        result.invoke(UiState.Failure(Strings.ERROR))
                    }
            }
        }
    }

    override fun deleteEmployer(
        employer: EmployerModel,
        result: (UiState<String>) -> Unit
    ) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employer.id)
                .collection(FireStoreTables.NOTES)
                .get()
                .addOnSuccessListener { employers ->
                    for (employer in employers) {
                        database
                            .collection(FireStoreTables.USER)
                            .document(user.uid)
                            .collection(FireStoreTables.EMPLOYERS)
                            .document(employer.id)
                            .collection(FireStoreTables.NOTES)
                            .document(employer.data["id"].toString())
                            .delete()
                    }
                }
                .addOnFailureListener {

                }

            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employer.id)
                .collection(FireStoreTables.TASKS)
                .get()
                .addOnSuccessListener { employers ->
                    for (employer in employers) {
                        database
                            .collection(FireStoreTables.USER)
                            .document(user.uid)
                            .collection(FireStoreTables.EMPLOYERS)
                            .document(employer.id)
                            .collection(FireStoreTables.TASKS)
                            .document(employer.data["id"].toString())
                            .delete()
                    }
                }
                .addOnFailureListener {

                }

            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employer.id)
                .delete()
                .addOnSuccessListener {
                    result.invoke(UiState.Success(Strings.DELETED))
                }
                .addOnFailureListener {
                    result.invoke(UiState.Failure(Strings.ERROR))
                }
        }
    }

    override fun getAllNotes(employer: EmployerModel, result: (UiState<List<NoteModel>>) -> Unit) {
        val notes = mutableListOf<NoteModel>()
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employer.id)
                .collection(FireStoreTables.NOTES)
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        notes += NoteModel(
                            document.data["id"].toString(),
                            document.data["noteTitle"].toString(),
                            document.data["noteSubTitle"].toString(),
                            document.data["noteDateTime"].toString()
                        )
                    }
                    result.invoke(UiState.Success(notes))
                }
                .addOnFailureListener {
                    result.invoke(UiState.Failure(Strings.ERROR))
                }
        }
    }

    override fun getNote(
        employer: EmployerModel,
        note: NoteModel,
        result: (UiState<NoteModel>) -> Unit
    ) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employer.id)
                .collection(FireStoreTables.NOTES)
                .document(note.id)
                .get()
                .addOnSuccessListener {
                    result.invoke(
                        UiState.Success(
                            it.data?.let { data ->
                                NoteModel(
                                    data["id"].toString(),
                                    data["noteTitle"].toString(),
                                    data["noteSubTitle"].toString(),
                                    data["noteDateTime"].toString()
                                )
                            }
                                ?: run {
                                    NoteModel()
                                }
                        )
                    )
                }
                .addOnFailureListener {
                    result.invoke(UiState.Failure(Strings.ERROR))
                }
        }
    }

    override fun addNote(
        employer: EmployerModel,
        note: NoteModel,
        result: (UiState<NoteModel>) -> Unit
    ) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employer.id)
                .collection(FireStoreTables.NOTES)
                .document(note.id)
                .set(note)
                .addOnSuccessListener {
                    result.invoke(UiState.Success(note))
                }
                .addOnFailureListener {
                    result.invoke(UiState.Failure(Strings.ERROR))
                }
        }
    }

    override fun updateNote(
        employer: EmployerModel,
        note: NoteModel,
        result: (UiState<String>) -> Unit
    ) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employer.id)
                .collection(FireStoreTables.NOTES)
                .document(note.id)
                .set(note)
                .addOnSuccessListener {
                    result.invoke(UiState.Success(Strings.UPDATED))
                }
                .addOnFailureListener {
                    result.invoke(UiState.Failure(Strings.ERROR))
                }
        }
    }

    override fun deleteNote(
        employer: EmployerModel,
        note: NoteModel,
        result: (UiState<String>) -> Unit
    ) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employer.id)
                .collection(FireStoreTables.NOTES)
                .document(note.id)
                .delete()
                .addOnSuccessListener {
                    result.invoke(UiState.Success(Strings.DELETED))
                }
                .addOnFailureListener {
                    result.invoke(UiState.Failure(Strings.ERROR))
                }
        }
    }

    override fun getAllTasks(employer: EmployerModel, result: (UiState<List<TaskModel>>) -> Unit) {
        val tasks = mutableListOf<TaskModel>()
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employer.id)
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

    override fun getTask(
        employer: EmployerModel,
        task: TaskModel,
        result: (UiState<TaskModel>) -> Unit
    ) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employer.id)
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

    override fun addTask(
        employer: EmployerModel,
        task: TaskModel,
        result: (UiState<TaskModel>) -> Unit
    ) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employer.id)
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

    override fun updateTask(
        employer: EmployerModel,
        task: TaskModel,
        result: (UiState<String>) -> Unit
    ) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employer.id)
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

    override fun deleteTask(
        employer: EmployerModel,
        task: TaskModel,
        result: (UiState<String>) -> Unit
    ) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employer.id)
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