package com.rg.headernotes.ui.notes

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.rg.headernotes.R
import com.rg.headernotes.databinding.FragmentAddNoteBinding
import com.rg.headernotes.models.NoteModel
import com.rg.headernotes.util.RequestCodes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class AddNoteFragment : Fragment() {
    private lateinit var binding: FragmentAddNoteBinding
    private var model: NoteModel?= null

    @SuppressLint("SimpleDateFormat")
    val format = SimpleDateFormat("dd.MM.yyyy HH:mm")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNoteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val initTime = Calendar.getInstance().timeInMillis
        binding.textViewDateTime.text = format.format(Date(initTime))

        arguments?.getParcelable(RequestCodes.editNote, NoteModel::class.java)?.let {
            model = it
            binding.editTextNoteTitle.setText(it.noteTitle)
            binding.editTextSubTitle.setText(it.noteSubTitle)
            binding.textViewDateTime.text = format.format(Date(it.noteDateTime.toLong()))
        }

        binding.buttonBack.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                parentFragmentManager.setFragmentResult(RequestCodes.setNote, Bundle())
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
                        noteDateTime = initTime.toString()
                    )
                    CoroutineScope(Dispatchers.Main).launch {
                        model?.let {
                            parentFragmentManager.setFragmentResult(RequestCodes.setNote, Bundle().apply {
                                putParcelable(RequestCodes.editNote, note)
                            })
                        } ?: run{
                            parentFragmentManager.setFragmentResult(RequestCodes.setNote, Bundle().apply {
                                putParcelable(RequestCodes.newNote, note)
                            })
                        }
                        parentFragmentManager.popBackStack()
                    }
                }
            }
        }
    }
}