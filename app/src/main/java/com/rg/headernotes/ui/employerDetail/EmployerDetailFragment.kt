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
import com.rg.headernotes.models.NoteModel
import com.rg.headernotes.models.TaskModel
import com.rg.headernotes.ui.notes.AddNoteFragment
import com.rg.headernotes.ui.notes.NoteAdapter
import com.rg.headernotes.ui.tasks.AddTaskFragment
import com.rg.headernotes.util.RequestCodes
import com.rg.headernotes.util.UiState
import com.rg.headernotes.viewModels.EmployersViewModel
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
    private val viewModel by viewModels<EmployersViewModel>()

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

        arguments?.getParcelable(RequestCodes.employerEdit, EmployerModel::class.java)?.let {
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
                parentFragmentManager.setFragmentResult(RequestCodes.setEmployer, Bundle())
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
                        id = model?.id.toString(),
                        fullName = binding.editTextName.text.toString(),
                        job = binding.editTextSpecies.text.toString(),
                        age = binding.editTextAge.text.toString(),
                        notesCount = adapterNotes.itemCount.toString(),
                        tasksCount = adapterTasks.itemCount.toString()
                    )
                    CoroutineScope(Dispatchers.Main).launch {
                        model?.let {
                            parentFragmentManager.setFragmentResult(
                                RequestCodes.setEmployer,
                                Bundle().apply {
                                    putParcelable(RequestCodes.employerEdit, employer)
                                })
                        } ?: run {
                            parentFragmentManager.setFragmentResult(
                                RequestCodes.setEmployer,
                                Bundle().apply {
                                    putParcelable(RequestCodes.employerEdit, employer)
                                })
                        }
                        parentFragmentManager.popBackStack()
                    }
                }
            }
        }

        binding.floatingActionButton.setOnClickListener {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerEmployerDetail, when(recyclerView.adapter){
                    adapterTasks -> {
                        AddTaskFragment()
                    }
                    else -> {
                        AddNoteFragment()
                    }
                }.apply {
                    arguments = Bundle().apply {
                        putBoolean(RequestCodes.employerEdit, true)
                    }
                })
                addToBackStack(null)
                commit()
            }
            binding.coordinatorLayout.visibility = View.INVISIBLE
        }

        model?.let { employerModel ->
            viewModel.getAllNotes(employerModel)
            viewModel.allNotes.observe(viewLifecycleOwner) { state ->
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

            viewModel.newNote.observe(viewLifecycleOwner) {
                when (it) {
                    is UiState.Loading -> {
                        binding.progressBarLoading.visibility = View.VISIBLE
                    }

                    is UiState.Success -> {
                        binding.progressBarLoading.visibility = View.INVISIBLE
                        adapterNotes.addNote(listOf(it.data))
                        binding.textViewListIsEmpty.visibility = if(adapterNotes.itemCount > 0) View.INVISIBLE else View.VISIBLE
                    }

                    is UiState.Failure -> {
                        binding.progressBarLoading.visibility = View.INVISIBLE
                    }
                }
            }
            viewModel.updateNote.observe(viewLifecycleOwner) {
                when (it) {
                    is UiState.Loading -> {
                        binding.progressBarLoading.visibility = View.VISIBLE
                    }

                    is UiState.Success -> {
                        viewModel.getAllNotes(employerModel)
                    }

                    is UiState.Failure -> {
                        binding.progressBarLoading.visibility = View.INVISIBLE
                    }
                }
            }

            viewModel.getAllTasks(employerModel)
            viewModel.allTasks.observe(viewLifecycleOwner) { state ->
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

            viewModel.updateTask.observe(viewLifecycleOwner) {
                when (it) {
                    is UiState.Loading -> {
                        binding.progressBarLoading.visibility = View.VISIBLE
                    }

                    is UiState.Success -> {
                        viewModel.getAllTasks(employerModel)
                        binding.textViewListIsEmpty.visibility = if(adapterTasks.itemCount > 0) View.INVISIBLE else View.VISIBLE
                    }

                    is UiState.Failure -> {
                        binding.progressBarLoading.visibility = View.INVISIBLE
                    }
                }
            }

            viewModel.newTask.observe(viewLifecycleOwner) {
                when (it) {
                    is UiState.Loading -> {
                        binding.progressBarLoading.visibility = View.VISIBLE
                    }

                    is UiState.Success -> {
                        binding.progressBarLoading.visibility = View.INVISIBLE
                        adapterTasks.addTask(listOf(it.data))
                        binding.textViewListIsEmpty.visibility = if(adapterTasks.itemCount > 0) View.INVISIBLE else View.VISIBLE
                    }

                    is UiState.Failure -> {
                        binding.progressBarLoading.visibility = View.INVISIBLE
                    }
                }
            }

            childFragmentManager.setFragmentResultListener(
                RequestCodes.setEmployer,
                viewLifecycleOwner
            ) { requestKey, result ->
                result.getParcelable(RequestCodes.newNote, NoteModel::class.java)?.let { note ->
                    val outModel = note.copy(id = adapterNotes.itemCount.toString())
                    viewModel.newNote(employerModel ,outModel)
                }
                result.getParcelable(RequestCodes.editNote, NoteModel::class.java)?.let { note ->
                    viewModel.updateNote(employerModel, note)
                }
                result.getParcelable(RequestCodes.newTask, TaskModel::class.java)?.let { task ->
                    val outModel = task.copy(id = adapterTasks.itemCount.toString())
                    viewModel.newTask(employerModel ,outModel)
                }
                result.getParcelable(RequestCodes.editTask, TaskModel::class.java)?.let { task ->
                    viewModel.updateTask(employerModel, task)
                }
                binding.coordinatorLayout.visibility = View.VISIBLE
            }
        }
    }
}