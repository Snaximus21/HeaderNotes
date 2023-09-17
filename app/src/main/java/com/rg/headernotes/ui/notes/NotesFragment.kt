package com.rg.headernotes.ui.notes

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
import com.rg.headernotes.databinding.FragmentNotesBinding
import com.rg.headernotes.util.ItemListener
import com.rg.headernotes.util.RequestCodes
import com.rg.headernotes.util.UiState
import com.rg.headernotes.util.showPopupMenu
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesFragment : Fragment(), ItemListener {
    private lateinit var binding: FragmentNotesBinding
    private val viewModel by viewModels<NotesViewModel>()
    private val adapter by lazy { NoteAdapter(this) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesBinding.inflate(layoutInflater, container, false)
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
                replace(R.id.fragmentContainerNotes, AddNoteFragment())
                addToBackStack(null)
                commit()
            }
            binding.coordinatorLayout.visibility = View.INVISIBLE
        }

        childFragmentManager.setFragmentResultListener(
            RequestCodes.setNote,
            viewLifecycleOwner
        ) { requestKey, result ->
            result.getParcelable(RequestCodes.newNote, NoteModel::class.java)?.let {
                viewModel.newNote(it)
            }
            result.getParcelable(RequestCodes.editNote, NoteModel::class.java)?.let {
                viewModel.updateNote(it)
            }
            binding.coordinatorLayout.visibility = View.VISIBLE
        }

        viewModel.getAllNotes()
        viewModel.allNotes.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                }

                is UiState.Success -> {
                    binding.progressBarLoading.visibility = View.INVISIBLE
                    adapter.setNotes(state.data)
                    binding.textViewListIsEmpty.visibility = if(adapter.itemCount > 0) View.INVISIBLE else View.VISIBLE
                }

                is UiState.Failure -> {
                    binding.progressBarLoading.visibility = View.INVISIBLE

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
                    adapter.addNote(listOf(it.data))
                    binding.textViewListIsEmpty.visibility = if(adapter.itemCount > 0) View.INVISIBLE else View.VISIBLE
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
                    viewModel.getAllNotes()
                }

                is UiState.Failure -> {
                    binding.progressBarLoading.visibility = View.INVISIBLE
                }
            }
        }
        viewModel.delete.observe(viewLifecycleOwner){
            if(it is UiState.Success){
                binding.textViewListIsEmpty.visibility = if(adapter.itemCount > 0) View.INVISIBLE else View.VISIBLE
            }
        }
    }

    override fun onItemClickListener(position: Int) {
        childFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerNotes, AddNoteFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(
                        RequestCodes.editNote,
                        adapter.getNote(position)
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
                            .setTitle("Удаление заметки")
                            .setMessage("Вы действительно хотите удалить заметку без возможности восстановления?")
                            .setNegativeButton("Удалить") { dialog, which ->
                                viewModel.deleteNote(adapter.getNote(position))
                                adapter.deleteNote(position)

                                dialog.dismiss()
                            }
                            .setPositiveButton("Отмена") { dialog, which ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                    R.id.editPopup -> {
                        childFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainerNotes, AddNoteFragment().apply {
                                arguments = Bundle().apply {
                                    putParcelable(
                                        RequestCodes.editNote,
                                        adapter.getNote(position)
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