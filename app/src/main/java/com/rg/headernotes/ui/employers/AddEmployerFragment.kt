package com.rg.headernotes.ui.employers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rg.headernotes.R
import com.rg.headernotes.databinding.FragmentAddEmployerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEmployerFragment : Fragment() {
    private lateinit var binding: FragmentAddEmployerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEmployerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                parentFragmentManager.setFragmentResult("newEmployer", Bundle())
                parentFragmentManager.popBackStack()
            }
        }

        binding.buttonApply.setOnClickListener {
            when{
                binding.editTextEmployerName.text.toString().trim().isEmpty() -> {
                    binding.editTextEmployerName.requestFocus()
                    binding.editTextEmployerName.error = getString(R.string.error_text_field)
                }
                binding.editTextEmployerTitleJob.text.toString().trim().isEmpty() -> {
                    binding.editTextEmployerTitleJob.requestFocus()
                    binding.editTextEmployerTitleJob.error = getString(R.string.error_text_field)
                }
                binding.editTextEmployerAge.text.toString().trim().isEmpty() -> {
                    binding.editTextEmployerAge.requestFocus()
                    binding.editTextEmployerAge.error = getString(R.string.error_text_field)
                }
                else -> {
                    val employer =  EmployerModel(
                        fullName = binding.editTextEmployerName.text.toString(),
                        job = binding.editTextEmployerTitleJob.text.toString(),
                        age = binding.editTextEmployerAge.text.toString()
                    )
                    CoroutineScope(Dispatchers.Main).launch {
                        parentFragmentManager.setFragmentResult("newEmployer", Bundle().apply {
                            putParcelable("employerModel", employer)
                        })
                        parentFragmentManager.popBackStack()
                    }
                }
            }
        }
    }

}