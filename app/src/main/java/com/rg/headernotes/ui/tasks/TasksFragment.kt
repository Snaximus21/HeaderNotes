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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rg.headernotes.R
import com.rg.headernotes.databinding.FragmentSettingsBinding
import com.rg.headernotes.databinding.FragmentTasksBinding
import com.rg.headernotes.ui.notes.AddNoteFragment
import com.rg.headernotes.ui.notes.NoteAdapter
import com.rg.headernotes.ui.notes.NoteModel
import com.rg.headernotes.ui.notes.NotesViewModel
import com.rg.headernotes.util.ItemListener
import com.rg.headernotes.util.RequestCodes
import com.rg.headernotes.util.UiState
import com.rg.headernotes.util.showPopupMenu
import com.rg.headertasks.ui.tasks.TaskAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TasksFragment : Fragment(), ItemListener {
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

        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        binding.floatingActionButton.setOnClickListener {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerTasks, AddTaskFragment())
                addToBackStack(null)
                commit()
            }
            binding.coordinatorLayout.visibility = View.INVISIBLE
        }

        childFragmentManager.setFragmentResultListener(
            RequestCodes.setTask,
            viewLifecycleOwner
        ) { _, result ->
            result.getParcelable(RequestCodes.newTask, TaskModel::class.java)?.let {
                viewModel.newTask(it)
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
                    recyclerView.resetPivot()
                }

                is UiState.Failure -> {
                    binding.progressBarLoading.visibility = View.INVISIBLE

                }
            }
        }

        viewModel.update.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                }

                is UiState.Success -> {
                    viewModel.getAllTasks()
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

        childFragmentManager.setFragmentResultListener(
            RequestCodes.setTask,
            viewLifecycleOwner
        ) { _, result ->
            result.getParcelable(RequestCodes.newTask, TaskModel::class.java)?.let {
                viewModel.newTask(it)
            }
            result.getParcelable(RequestCodes.editTask, TaskModel::class.java)?.let {
                viewModel.updateTask(it)
            }
            binding.coordinatorLayout.visibility = View.VISIBLE
        }
    }

    override fun onItemClickListener(position: Int) {
        childFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerTasks, AddTaskFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(
                        RequestCodes.editTask,
                        adapter.getTask(position)
                    )
                }
            })
            addToBackStack(null)
            commit()
        }
        binding.coordinatorLayout.visibility = View.INVISIBLE
    }

    override fun onLongItemClickListener(view: View, position: Int) {
        showPopupMenu(
            view,
            R.menu.item_menu,
            { menu ->
                when(menu.itemId){
                    R.id.deletePopup -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Удаление задачи")
                            .setMessage("Вы действительно хотите удалить задачу без возможности восстановления?")
                            .setNegativeButton("Удалить") { dialog, which ->
                                viewModel.deleteTask(adapter.getTask(position))
                                adapter.deleteTask(position)

                                dialog.dismiss()
                            }
                            .setPositiveButton("Отмена") { dialog, which ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                    R.id.editPopup -> {
                        childFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainerTasks, AddTaskFragment().apply {
                                arguments = Bundle().apply {
                                    putParcelable(
                                        RequestCodes.editTask,
                                        adapter.getTask(position)
                                    )
                                }
                            })
                            addToBackStack(null)
                            commit()
                        }
                        binding.coordinatorLayout.visibility = View.INVISIBLE
                    }
                }
            }
        )
    }
}