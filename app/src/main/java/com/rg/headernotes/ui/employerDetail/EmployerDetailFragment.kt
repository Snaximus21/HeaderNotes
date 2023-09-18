package com.rg.headernotes.ui.employerDetail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rg.headernotes.R
import com.rg.headernotes.databinding.FragmentEmployerDetailBinding
import com.rg.headernotes.models.EmployerModel
import com.rg.headernotes.ui.notes.NoteAdapter
import com.rg.headernotes.ui.tasks.TaskModel
import com.rg.headernotes.util.RequestCodes
import com.rg.headernotes.util.Strings
import com.rg.headernotes.util.UiState
import com.rg.headernotes.util.setAppTheme
import com.rg.headernotes.viewModels.NotesViewModel
import com.rg.headernotes.viewModels.TasksViewModel
import com.rg.headertasks.ui.tasks.TaskAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class EmployerDetailFragment : Fragment() {
    private lateinit var binding: FragmentEmployerDetailBinding
    private var model: EmployerModel? = null
    private val adapterNotes by lazy { NoteAdapter(null) }
    private val adapterTasks by lazy { TaskAdapter(null) }
    private val viewModelTasks by viewModels<TasksViewModel>()
    private val viewModelNotes by viewModels<NotesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmployerDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable(RequestCodes.employerDetail, EmployerModel::class.java)?.let {
            model = it
            binding.editTextName.setText(it.fullName)
            binding.editTextSpecies.setText(it.job)
            binding.editTextAge.setText(it.age)
        }

        val recyclerView = binding.recyclerView
        val layoutManager = LinearLayoutManager(requireActivity())

        recyclerView.adapter = adapterTasks
        recyclerView.layoutManager = layoutManager

        binding.toggleButtonGroup.addOnButtonCheckedListener { toggleButton, _, _ ->
            when (toggleButton.checkedButtonId) {
                binding.buttonTasks.id -> {
                    recyclerView.adapter = adapterTasks
                }

                binding.buttonNotes.id -> {
                    recyclerView.adapter = adapterNotes
                }

                else -> {
                }
            }
        }

        binding.buttonBack.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                parentFragmentManager.setFragmentResult(RequestCodes.employerDetail, Bundle())
                parentFragmentManager.popBackStack()
            }
        }

        binding.buttonApply.setOnClickListener {
            when {
                binding.editTextName.text.toString().trim().isEmpty() -> {
                    binding.editTextName.requestFocus()
                    binding.editTextName.error = getString(R.string.error_text_field)
                }

                binding.editTextSpecies.text.toString().trim().isEmpty() -> {
                    binding.editTextSpecies.requestFocus()
                    binding.editTextSpecies.error = getString(R.string.error_text_field)
                }

                binding.editTextAge.text.toString().trim().isEmpty() -> {
                    binding.editTextAge.requestFocus()
                    binding.editTextAge.error = getString(R.string.error_text_field)
                }

                else -> {
                    val employer = EmployerModel(
                        fullName = binding.editTextName.text.toString(),
                        job = binding.editTextSpecies.text.toString(),
                        age = binding.editTextAge.text.toString()
                    )
                    CoroutineScope(Dispatchers.Main).launch {
                        model?.let {
                            parentFragmentManager.setFragmentResult(
                                RequestCodes.employerDetail,
                                Bundle().apply {
                                    putParcelable(RequestCodes.employerEdit, employer)
                                })
                        } ?: run {
                            parentFragmentManager.setFragmentResult(
                                RequestCodes.employerDetail,
                                Bundle().apply {
                                    putParcelable(RequestCodes.employerEdit, employer)
                                })
                        }
                        parentFragmentManager.popBackStack()
                    }
                }
            }
        }



        viewModelTasks.getAllTasks()
        viewModelTasks.allTasks.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }

                is UiState.Success -> {
                    adapterTasks.setTasks(state.data)
                    recyclerView.resetPivot()
                }

                is UiState.Failure -> {

                }
            }
        }

        viewModelNotes.getAllNotes()
        viewModelNotes.allNotes.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {

                }

                is UiState.Success -> {
                    adapterNotes.setNotes(state.data)
                    recyclerView.resetPivot()
                }

                is UiState.Failure -> {

                }
            }
        }
    }
}