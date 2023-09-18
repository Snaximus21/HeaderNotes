package com.rg.headernotes.ui.notes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rg.headernotes.databinding.ItemNoteBinding
import com.rg.headernotes.models.NoteModel
import com.rg.headernotes.util.ItemListener

class NoteAdapter(private val itemListener: ItemListener) : RecyclerView.Adapter<NoteViewHolder>() {
    private var notesList = emptyList<NoteModel>()

    fun getNote(pos: Int) : NoteModel {
        if(pos > notesList.size) return NoteModel()
        return notesList[pos]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setNotes(notes: List<NoteModel>){
        notesList = notes
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addNote(note: List<NoteModel>){
        notesList += note
        notifyItemInserted(itemCount)
    }

    fun deleteNote(position: Int){
        notesList = notesList.toMutableList().apply {
            removeAt(position)
        }.toList()
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false), itemListener)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notesList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
}