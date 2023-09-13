package com.rg.headernotes.ui.employers

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.rg.headernotes.databinding.ItemEmployerBinding

class EmployerViewHolder(private val binding: ItemEmployerBinding) : ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(model: EmployerModel){
        binding.textViewAge.text = model.fullName
        binding.textViewJob.text = "Должность: ${model.job}"
        binding.textViewAge.text = "Возраст: ${model.age}"
        binding.textViewNotes.text = "Заметки: ${model.notesCount}"
        binding.textViewNotes.text = "Задачи: ${model.tasksCount}"
    }
}