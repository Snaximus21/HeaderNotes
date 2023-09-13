package com.rg.headernotes.ui.employers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.rg.headernotes.databinding.ItemEmployerBinding

class EmployerAdapter() : Adapter<EmployerViewHolder>() {
    private var employersList = emptyList<EmployerModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setEmployers(employers: List<EmployerModel>){
        employersList = employers
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addEmployer(employer: List<EmployerModel>){
        employersList += employer
        notifyItemInserted(itemCount)
    }

    fun deleteEmployer(position: Int){
        employersList = employersList.toMutableList().apply {
            removeAt(position)
        }.toList()
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return employersList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployerViewHolder {
        return EmployerViewHolder(ItemEmployerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: EmployerViewHolder, position: Int) {
        holder.bind(employersList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
}