package com.rg.headernotes.dao

import com.rg.headernotes.models.NoteModel
import com.rg.headernotes.util.UiState

interface NotesDao {
    fun getAllNotes(result: (UiState<List<NoteModel>>) -> Unit)
    fun getNote(note: NoteModel, result: (UiState<NoteModel>) -> Unit)
    fun addNote(note: NoteModel, result: (UiState<NoteModel>) -> Unit)
    fun updateNote(note: NoteModel, result: (UiState<String>) -> Unit)
    fun deleteNote(note: NoteModel, result: (UiState<String>) -> Unit)
}