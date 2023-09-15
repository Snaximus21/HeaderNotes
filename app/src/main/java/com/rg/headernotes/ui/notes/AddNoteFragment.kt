package com.rg.headernotes.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rg.headernotes.R
import com.rg.headernotes.databinding.FragmentAddNoteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class AddNoteFragment : Fragment() {
    private lateinit var binding: FragmentAddNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNoteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                parentFragmentManager.setFragmentResult("newNote", Bundle())
                parentFragmentManager.popBackStack()
            }
        }

        binding.buttonApply.setOnClickListener {
            when{
                binding.editTextNoteTitle.text.toString().trim().isEmpty() -> {
                    binding.editTextNoteTitle.requestFocus()
                    binding.editTextNoteTitle.error = getString(R.string.error_text_field)
                }
                else -> {
                    val note =  NoteModel(
                        noteTitle = binding.editTextNoteTitle.text.toString(),
                        noteSubTitle = binding.editTextSubTitle.text.toString(),
                        noteDateTime = Calendar.getInstance().timeInMillis.toString()
                    )
                    CoroutineScope(Dispatchers.Main).launch {
                        parentFragmentManager.setFragmentResult("newNote", Bundle().apply {
                            putParcelable("noteModel", note)
                        })
                        parentFragmentManager.popBackStack()
                    }
                }
            }
        }
    }
}