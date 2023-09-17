package com.rg.headernotes.ui.notes

import android.view.View

interface NoteListener {
    fun onNoteClickListener(position: Int)
    fun onLongNoteClickListener(view: View, position: Int)
}