package com.rg.headernotes.ui.employers

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.rg.headernotes.databinding.ItemEmployerBinding
import com.rg.headernotes.models.EmployerModel
import com.rg.headernotes.util.isElementNull

class EmployerViewHolder(private val binding: ItemEmployerBinding) : ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(model: EmployerModel){
        binding.textViewName.text = model.fullName
        binding.textViewJob.text = "Должность: ${model.job}"
        binding.textViewAge.text = "Возраст: ${model.age}"
        if(model.notesCount.isElementNull())//Todo доделать
            binding.textViewNotes.text = "Заметки: отсутствуют"
        else
            binding.textViewNotes.text = "Заметки: ${model.notesCount}"
        if(model.tasksCount.isElementNull())//Todo доделать
            binding.textViewTasks.text = "Задачи: отсутствуют"
        else
            binding.textViewTasks.text = "Заметки: ${model.notesCount}"
    }
}