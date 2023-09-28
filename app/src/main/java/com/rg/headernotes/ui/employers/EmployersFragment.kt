package com.rg.headernotes.ui.employers

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rg.headernotes.R
import com.rg.headernotes.databinding.FragmentEmployersBinding
import com.rg.headernotes.models.EmployerModel
import com.rg.headernotes.models.NoteModel
import com.rg.headernotes.ui.employerDetail.EmployerDetailFragment
import com.rg.headernotes.ui.notes.AddNoteFragment
import com.rg.headernotes.util.ItemListener
import com.rg.headernotes.util.RequestCodes
import com.rg.headernotes.viewModels.EmployersViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.rg.headernotes.util.UiState

@AndroidEntryPoint
class EmployersFragment : Fragment(), ItemListener {
    private lateinit var binding: FragmentEmployersBinding
    private val viewModel by viewModels<EmployersViewModel>()
    private val adapter by lazy { EmployerAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmployersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerView
        val layoutManager = LinearLayoutManager(requireActivity())

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

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

        binding.floatingActionButtonEmployers.setOnClickListener {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerEmployers, AddEmployerFragment())
                addToBackStack(null)
                commit()
            }
            binding.coordinatorLayout.visibility = View.INVISIBLE
        }

        viewModel.getAllEmployers()
        viewModel.allEmployers.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                }

                is UiState.Success -> {
                    binding.progressBarLoading.visibility = View.INVISIBLE
                    adapter.setEmployers(state.data)
                    binding.textViewListIsEmpty.visibility = if(adapter.itemCount > 0) View.INVISIBLE else View.VISIBLE
                }

                is UiState.Failure -> {
                    binding.progressBarLoading.visibility = View.INVISIBLE

                }
            }
        }

        viewModel.newEmployer.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                }

                is UiState.Success -> {
                    binding.progressBarLoading.visibility = View.INVISIBLE
                    adapter.addEmployer(listOf(it.data))
                    binding.textViewListIsEmpty.visibility = if(adapter.itemCount > 0) View.INVISIBLE else View.VISIBLE
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

        viewModel.update.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                }

                is UiState.Success -> {
                    viewModel.getAllEmployers()
                    binding.textViewListIsEmpty.visibility = if(adapter.itemCount > 0) View.INVISIBLE else View.VISIBLE
                }

                is UiState.Failure -> {
                    binding.progressBarLoading.visibility = View.INVISIBLE
                }
            }
        }

        ItemTouchHelper(object : ItemTouchHelper.Callback() {

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.LEFT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                viewHolder.itemView.apply {
                    ContextCompat.getDrawable(context, R.drawable.icon_delete)?.apply {
                        val background = ColorDrawable()
                        background.color = ContextCompat.getColor(context, R.color.red)

                        val iconMargin = ((bottom - top) - intrinsicHeight) / 2
                        val iconTop = top + iconMargin
                        val iconBottom = iconTop + intrinsicHeight
                        val iconRight = right - iconMargin
                        val iconLeft = iconRight - intrinsicWidth

                        setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        background.setBounds(right + dX.toInt(), top, right, bottom)

                        background.draw(c)
                        draw(c)
                    }
                }

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return 0.2f
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Удаление сотрудника")
                    .setMessage("Вы действительно хотите удалить сотрудника без возможности восстановления?")
                    .setNegativeButton("Удалить") { dialog, which ->
                        viewModel.deleteEmployer(adapter.getEmployer(viewHolder.adapterPosition))
                        adapter.deleteEmployer(viewHolder.adapterPosition)

                        dialog.dismiss()
                    }
                    .setPositiveButton("Отмена") { dialog, which ->
                        adapter.notifyItemChanged(viewHolder.adapterPosition)
                        clearView(recyclerView, viewHolder)
                        recyclerView.resetPivot()
                        dialog.dismiss()
                    }
                    .show()
            }
        }).attachToRecyclerView(recyclerView)

        childFragmentManager.setFragmentResultListener(
            RequestCodes.setEmployer,
            viewLifecycleOwner
        ) { requestKey, result ->
            result.getParcelable(RequestCodes.employerEdit, EmployerModel::class.java)?.let {
                viewModel.updateEmployer(it)
            }
            result.getParcelable(RequestCodes.newEmployer, EmployerModel::class.java)?.let {
                val outModel = it.copy(id = adapter.itemCount.toString())
                viewModel.newEmployer(outModel)
            }
            binding.coordinatorLayout.visibility = View.VISIBLE
        }
    }

    override fun onItemClickListener(position: Int) {
        childFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerEmployers, EmployerDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(
                        RequestCodes.employerEdit,
                        adapter.getEmployer(position)
                    )
                }
            })
            addToBackStack(null)
            commit()
        }
        binding.coordinatorLayout.visibility = View.INVISIBLE
    }

    override fun onLongItemClickListener(view: View, position: Int) {
    }
}