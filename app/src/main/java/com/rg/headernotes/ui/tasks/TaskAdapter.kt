package com.rg.headertasks.ui.tasks

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rg.headernotes.databinding.ItemTaskBinding
import com.rg.headernotes.ui.tasks.TaskModel
import com.rg.headernotes.ui.tasks.TaskViewHolder
import com.rg.headernotes.util.ItemListener

class TaskAdapter(
    private val itemListener: ItemListener
) : RecyclerView.Adapter<TaskViewHolder>() {
    private var tasksList = emptyList<TaskModel>()

    fun getTask(pos: Int) : TaskModel {
        if(pos > tasksList.size) return TaskModel()
        return tasksList[pos]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setTasks(tasks: List<TaskModel>){
        tasksList = tasks
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addTask(task: List<TaskModel>){
        tasksList += task
        notifyItemInserted(itemCount)
    }

    fun deleteTask(position: Int){
        tasksList = tasksList.toMutableList().apply {
            removeAt(position)
        }.toList()
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false), itemListener)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasksList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
}