package com.rg.headernotes.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rg.headernotes.dao.NotesDao
import com.rg.headernotes.models.NoteModel
import com.rg.headernotes.util.FireStoreTables
import com.rg.headernotes.util.Strings
import com.rg.headernotes.util.UiState
import javax.inject.Inject

class NotesRepository @Inject constructor(private val database: FirebaseFirestore) : NotesDao {
    override fun getAllNotes(result: (UiState<List<NoteModel>>) -> Unit) {
        val notes = mutableListOf<NoteModel>()
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
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

    override fun getNote(note: NoteModel, result: (UiState<NoteModel>) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
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

    override fun addNote(note: NoteModel, result: (UiState<NoteModel>) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
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

    override fun updateNote(note: NoteModel, result: (UiState<String>) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
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

    override fun deleteNote(note: NoteModel, result: (UiState<String>) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database
                .collection(FireStoreTables.USER)
                .document(user.uid)
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
}