package com.rg.headernotes.ui.employers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.rg.headernotes.databinding.ItemEmployerBinding
import com.rg.headernotes.models.EmployerModel


class EmployerAdapter() : Adapter<EmployerViewHolder>() {
    private var employersList = emptyList<EmployerModel>()

    fun getEmployer(pos: Int) : EmployerModel {
        if(pos > employersList.size) return EmployerModel()
        return employersList[pos]
    }

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

    fun restoreEmployer(employer: EmployerModel, position: Int) {
        employersList.toMutableList().apply {
            add(position, employer)
        }
        notifyItemInserted(position)
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