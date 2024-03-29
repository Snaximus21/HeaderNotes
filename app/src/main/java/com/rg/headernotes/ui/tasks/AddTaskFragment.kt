package com.rg.headernotes.ui.tasks

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.rg.headernotes.R
import com.rg.headernotes.databinding.FragmentAddTaskBinding
import com.rg.headernotes.models.TaskModel
import com.rg.headernotes.util.DateTimeConverter.currentDateTimeMillis
import com.rg.headernotes.util.DateTimeConverter.toDate
import com.rg.headernotes.util.DateTimeConverter.toDateTime
import com.rg.headernotes.util.RequestCodes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class AddTaskFragment : Fragment() {
    private lateinit var binding: FragmentAddTaskBinding
    private var model: TaskModel? = null
    private var pickerTime: Long? = null
    private var isEmployer: Boolean = false

    @SuppressLint("SimpleDateFormat")
    val format = SimpleDateFormat("dd.MM.yyyy")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable(RequestCodes.editTask, TaskModel::class.java)?.let {
            model = it
            binding.editTextTaskName.setText(it.taskName)
            binding.editTextTitle.setText(it.taskNote)
            binding.buttonDate.text = it.taskDate.toLong().toDate()
        }

        isEmployer = arguments?.getBoolean(RequestCodes.employerEdit) == true

        binding.buttonBack.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                parentFragmentManager.setFragmentResult(RequestCodes.setTask, Bundle())
                parentFragmentManager.popBackStack()
            }
        }

        binding.buttonDate.setOnClickListener {
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Крайний срок")
                .setSelection(
                    when {
                        model != null -> model?.taskDate?.toLong()
                        else -> currentDateTimeMillis()
                    }
                )
                .build().apply {
                    addOnPositiveButtonClickListener {
                        binding.buttonDate.text = format.format(Date(it))
                        pickerTime = it
                    }
                    addOnNegativeButtonClickListener {

                    }
                }.show(parentFragmentManager, "Крайний срок")
        }
        binding.buttonApply.setOnClickListener {
            when {
                binding.editTextTaskName.text.toString().trim().isEmpty() -> {
                    binding.editTextTaskName.requestFocus()
                    binding.editTextTaskName.error = getString(R.string.error_text_field)
                }

                else -> {
                    val task = TaskModel(
                        id = model?.id.toString(),
                        taskName = binding.editTextTaskName.text.toString(),
                        taskNote = binding.editTextTitle.text.toString(),
                        taskDate = when {
                            pickerTime != null -> pickerTime.toString()
                            model != null -> model?.taskDate.toString()
                            else -> currentDateTimeMillis().toString()
                        }
                    )

                    CoroutineScope(Dispatchers.Main).launch {
                        model?.let {
                            parentFragmentManager.setFragmentResult(
                                if (isEmployer) RequestCodes.setEmployer else RequestCodes.setTask,
                                Bundle().apply {
                                    putParcelable(RequestCodes.editTask, task)
                                })
                        } ?: run {
                            parentFragmentManager.setFragmentResult(
                                if (isEmployer) RequestCodes.setEmployer else RequestCodes.setTask,
                                Bundle().apply {
                                    putParcelable(RequestCodes.newTask, task)
                                })
                        }
                        parentFragmentManager.popBackStack()
                    }
                }
            }
        }
    }
}