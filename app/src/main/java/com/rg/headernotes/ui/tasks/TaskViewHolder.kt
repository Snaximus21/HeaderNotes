package com.rg.headernotes.ui.tasks

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.rg.headernotes.databinding.ItemNoteBinding
import com.rg.headernotes.databinding.ItemTaskBinding
import com.rg.headernotes.ui.notes.NoteModel
import com.rg.headernotes.util.ItemListener
import com.rg.headernotes.util.isElementNull
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class TaskViewHolder(private val binding: ItemTaskBinding, private val itemListener: ItemListener) :
    RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SimpleDateFormat")
    val format = SimpleDateFormat("dd.MM.yyyy HH:mm")

    @SuppressLint("SetTextI18n")
    fun bind(model: TaskModel) {
        binding.textViewTitle.text = model.taskName
        binding.textViewTask.text = model.taskNote

        val time = if (model.taskDate.isElementNull()) "" else format.format(Date(model.taskDate.toLong()))
        val timeLow = kotlin.math.abs(model.taskDate.toLong() - Calendar.getInstance().timeInMillis) < 86400000
        if (timeLow) binding.textViewDateTime.setTextColor(Color.RED)

        binding.textViewDateTime.text = time

        binding.taskItem.setOnClickListener {
            itemListener.onItemClickListener(adapterPosition)
        }
        binding.taskItem.setOnLongClickListener {
            itemListener.onLongItemClickListener(binding.root, adapterPosition)
            true
        }
    }
}