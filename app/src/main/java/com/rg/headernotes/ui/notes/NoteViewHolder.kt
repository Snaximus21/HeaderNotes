package com.rg.headernotes.ui.notes

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.rg.headernotes.databinding.ItemNoteBinding
import com.rg.headernotes.models.NoteModel
import com.rg.headernotes.util.ItemListener
import com.rg.headernotes.util.isElementNull
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class NoteViewHolder (private val binding: ItemNoteBinding, private val itemListener: ItemListener?) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SimpleDateFormat")
    val format = SimpleDateFormat("dd.MM.yyyy HH:mm")
    @SuppressLint("SetTextI18n")
    fun bind(model: NoteModel){
        binding.textViewTitle.text = model.noteTitle
        binding.textViewSubTitle.text = model.noteSubTitle

        val time = if (model.noteDateTime.isElementNull())
            Calendar.getInstance().timeInMillis
        else
            model.noteDateTime.toLong()

        binding.textViewDateTime.text = format.format(Date(time))
        binding.noteItem.setOnClickListener {
            itemListener?.onItemClickListener(adapterPosition)
        }
        binding.noteItem.setOnLongClickListener {
            itemListener?.onLongItemClickListener(binding.root ,adapterPosition)
            true
        }
    }
}