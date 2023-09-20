package com.rg.headernotes.ui.tasks

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.rg.headernotes.databinding.ItemTaskBinding
import com.rg.headernotes.models.TaskModel
import com.rg.headernotes.util.DateTimeConverter.currentDateTimeMillis
import com.rg.headernotes.util.DateTimeConverter.toDateTime
import com.rg.headernotes.util.ItemListener
import com.rg.headernotes.util.isElementNull

class TaskViewHolder(
    private val binding: ItemTaskBinding,
    private val itemListener: ItemListener?
) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(model: TaskModel) {
        binding.textViewTitle.text = model.taskName
        binding.textViewTask.text = model.taskNote

        val time = if (model.taskDate.isElementNull())
            currentDateTimeMillis()
         else
            model.taskDate.toLong()


        val timeLow = (time - currentDateTimeMillis()) < 86400000
        if (timeLow)
            binding.imageViewTask.setColorFilter(Color.RED)

        binding.textViewDateTime.text = time.toDateTime()

        binding.taskItem.setOnClickListener {
            itemListener?.onItemClickListener(adapterPosition)
        }
        binding.taskItem.setOnLongClickListener {
            itemListener?.onLongItemClickListener(binding.root, adapterPosition)
            true
        }
    }
}