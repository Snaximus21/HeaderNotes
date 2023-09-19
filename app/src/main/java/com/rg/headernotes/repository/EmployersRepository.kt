package com.rg.headernotes.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rg.headernotes.dao.EmployersDao
import com.rg.headernotes.models.EmployerModel
import com.rg.headernotes.models.NoteModel
import com.rg.headernotes.ui.tasks.TaskModel
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
                            document.data["fullName"].toString(),
                            document.data["job"].toString(),
                            document.data["age"].toString(),
                            document.data["notesCount"].toString(),
                            document.data["tasksCount"].toString(),
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

    override fun addEmployer(
        employer: EmployerModel,
        employerName: String,
        result: (UiState<EmployerModel>) -> Unit
    ) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employerName)
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
                    .document(employer.fullName)
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
        employerName: String,
        result: (UiState<String>) -> Unit
    ) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employerName)
                .collection(FireStoreTables.NOTES)
                .get()
                .addOnSuccessListener { employers ->
                    for (employer in employers) {
                        database
                            .collection(FireStoreTables.USER)
                            .document(user.uid)
                            .collection(FireStoreTables.EMPLOYERS)
                            .document(employerName)
                            .collection(FireStoreTables.NOTES)
                            .document(employer.data["noteTitle"].toString())
                            .delete()
                    }
                }
                .addOnFailureListener {

                }

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

    override fun getAllNotes(employer: EmployerModel, result: (UiState<List<NoteModel>>) -> Unit) {
        val notes = mutableListOf<NoteModel>()
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
                .collection(FireStoreTables.EMPLOYERS)
                .document(employer.fullName)
                .collection(FireStoreTables.NOTES)
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        notes += NoteModel(
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
                .document(employer.fullName)
                .collection(FireStoreTables.NOTES)
                .document(note.noteTitle)
                .get()
                .addOnSuccessListener {
                    result.invoke(
                        UiState.Success(
                            it.data?.let { data ->
                                NoteModel(
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
                .document(employer.fullName)
                .collection(FireStoreTables.NOTES)
                .document(note.noteTitle)
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
                .document(employer.fullName)
                .collection(FireStoreTables.NOTES)
                .document(note.noteTitle)
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
                .document(employer.fullName)
                .collection(FireStoreTables.NOTES)
                .document(note.noteTitle)
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
                .document(employer.fullName)
                .collection(FireStoreTables.TASKS)
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        tasks += TaskModel(
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
                .document(employer.fullName)
                .collection(FireStoreTables.TASKS)
                .document(task.taskName)
                .get()
                .addOnSuccessListener {
                    result.invoke(
                        UiState.Success(
                            it.data?.let { data ->
                                TaskModel(
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
                .document(employer.fullName)
                .collection(FireStoreTables.TASKS)
                .document(task.taskName)
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
                .document(employer.fullName)
                .collection(FireStoreTables.TASKS)
                .document(task.taskName)
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
                .document(employer.fullName)
                .collection(FireStoreTables.TASKS)
                .document(task.taskName)
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