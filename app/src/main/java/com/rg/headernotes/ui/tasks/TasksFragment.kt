package com.rg.headernotes.ui.tasks

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.rg.headernotes.R
import com.rg.headernotes.databinding.FragmentSettingsBinding
import com.rg.headernotes.databinding.FragmentTasksBinding
import com.rg.headernotes.ui.notes.AddNoteFragment
import com.rg.headernotes.ui.notes.NoteAdapter
import com.rg.headernotes.ui.notes.NoteModel
import com.rg.headernotes.ui.notes.NotesViewModel
import com.rg.headernotes.util.UiState
import com.rg.headertasks.ui.tasks.TaskAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksFragment : Fragment(), TaskLitener {
    private lateinit var binding: FragmentTasksBinding
    private val adapter by lazy { TaskAdapter(this) }
    private val viewModel by viewModels<TasksViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    childFragmentManager.apply {
                        when {
                            backStackEntryCount > 0 -> {
                                if (backStackEntryCount == 1)
                                    binding.coordinatorLayout.visibility = View.VISIBLE
                                popBackStackImmediate()
                            }

                            else -> {
                                isEnabled = false
                                requireActivity().onBackPressedDispatcher.onBackPressed()
                            }
                        }
                    }
                }
            }
        )

        val recyclerView = binding.recyclerView
        val layoutManager = LinearLayoutManager(requireActivity())

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        binding.floatingActionButton.setOnClickListener {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerNotes, AddNoteFragment().apply {
                    arguments = Bundle().apply { putParcelable("newTask", TaskModel()) }
                })
                addToBackStack(null)
                commit()
            }
            binding.coordinatorLayout.visibility = View.INVISIBLE
        }

        childFragmentManager.setFragmentResultListener(
            "newTask",
            viewLifecycleOwner
        ) { requestKey, result ->
            if (requestKey == "newTask") {
                result.getParcelable("taskModel", TaskModel::class.java)?.let {
                    viewModel.newTask(it)
                }
            }
            binding.coordinatorLayout.visibility = View.VISIBLE
        }

        viewModel.getAllTasks()
        viewModel.allTasks.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                }

                is UiState.Success -> {
                    binding.progressBarLoading.visibility = View.INVISIBLE
                    adapter.setTasks(state.data)
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
                    adapter.addTask(listOf(it.data))
                }

                is UiState.Failure -> {
                    binding.progressBarLoading.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onButtonDoneClicked(position: Int) {
        viewModel.deleteTask(adapter.getTask(position))
        adapter.deleteTask(position)
    }

    override fun onButtonPostponeClicked(position: Int) {

    }
}