package com.rg.headernotes.ui.employers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rg.headernotes.databinding.FragmentEmployersBinding
import dagger.hilt.android.AndroidEntryPoint
import com.rg.headernotes.util.UiState

@AndroidEntryPoint
class EmployersFragment : Fragment() {
    private val viewModel by viewModels<EmployersViewModel>()
    private val binding by lazy { FragmentEmployersBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.allEmployers.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loading -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    binding.progressBarLoading.visibility = View.INVISIBLE
                }
                is UiState.Failure -> {
                    binding.progressBarLoading.visibility = View.INVISIBLE

                }
            }
        }
    }
}