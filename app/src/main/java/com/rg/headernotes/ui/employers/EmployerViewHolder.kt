package com.rg.headernotes.ui.employers

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.rg.headernotes.databinding.ItemEmployerBinding
import com.rg.headernotes.models.EmployerModel
import com.rg.headernotes.util.ItemListener
import com.rg.headernotes.util.isElementNull

class EmployerViewHolder(private val binding: ItemEmployerBinding, private val itemListener: ItemListener?) : ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(model: EmployerModel){
        binding.textViewName.text = model.fullName
        binding.textViewJob.text = "Должность: ${model.job}"
        binding.textViewAge.text = "Возраст: ${model.age}"

        binding.textViewNotes.text = when{
            model.notesCount.isElementNull() ->  "Заметки: отсутствуют"
            (model.notesCount.toInt() == 0) -> "Заметки: отсутствуют"
            else -> "Заметки: ${model.notesCount}"
        }

        binding.textViewTasks.text = when{
            model.tasksCount.isElementNull() ->  "Задачи: отсутствуют"
            (model.tasksCount.toInt() == 0) -> "Задачи: отсутствуют"
            else -> "Задачи: ${model.tasksCount}"
        }

        binding.employerItem.setOnClickListener {
            itemListener?.onItemClickListener(adapterPosition)
        }
        binding.employerItem.setOnLongClickListener {
            itemListener?.onLongItemClickListener(binding.root ,adapterPosition)
            true
        }
    }
}