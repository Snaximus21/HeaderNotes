package com.rg.headernotes.ui.tasks

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.rg.headernotes.databinding.ItemNoteBinding
import com.rg.headernotes.databinding.ItemTaskBinding
import com.rg.headernotes.ui.notes.NoteModel
import com.rg.headernotes.util.isElementNull
import java.text.SimpleDateFormat
import java.util.Date

class TaskViewHolder(private val binding: ItemTaskBinding, private val taskListener: TaskLitener) :
    RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SimpleDateFormat")
    val format = SimpleDateFormat("dd.MM.yyyy HH:mm")

    @SuppressLint("SetTextI18n")
    fun bind(model: TaskModel) {
        binding.textViewTitle.text = model.taskName
        binding.textViewTask.text = model.taskNote

        val time = if (model.taskDate.isElementNull()) "" else format.format(Date(model.taskDate.toLong()))
        binding.textViewDateTime.text = time

        binding.buttonDone.setOnClickListener {
            taskListener.onButtonDoneClicked(adapterPosition)
        }
        binding.buttonPostpone.setOnClickListener {
            taskListener.onButtonPostponeClicked(adapterPosition)
        }
    }
}