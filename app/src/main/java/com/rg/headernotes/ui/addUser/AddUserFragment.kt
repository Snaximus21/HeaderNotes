package com.rg.headernotes.ui.addUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.rg.headernotes.R
import com.rg.headernotes.databinding.FragmentAddUserBinding
import com.rg.headernotes.util.GraphActions
import com.rg.headernotes.util.UiState
import com.rg.headernotes.util.showMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUserFragment : Fragment() {
    private lateinit var binding: FragmentAddUserBinding
    private val viewModel by viewModels<AddUserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddUserBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonApply.setOnClickListener {
            when{
                binding.editTextHeaderName.text.toString().trim().isEmpty() -> {
                    binding.editTextHeaderName.requestFocus()
                    binding.editTextHeaderName.error = getString(R.string.error_text_field)
                }
                binding.editTextSubvision.text.toString().trim().isEmpty() -> {
                    binding.editTextSubvision.requestFocus()
                    binding.editTextSubvision.error = getString(R.string.error_text_field)
                }
                else -> {
                    viewModel.addUser(
                        UserModel(
                            binding.editTextHeaderName.text.toString().trim(),
                            binding.editTextSubvision.text.toString().trim()
                        )
                    )
                }
            }
        }
        viewModel.addNewUser.observe(viewLifecycleOwner){
            if(it is UiState.Success){
                Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                    .navigate(GraphActions.addUserToMain)
            }
        }
    }
}