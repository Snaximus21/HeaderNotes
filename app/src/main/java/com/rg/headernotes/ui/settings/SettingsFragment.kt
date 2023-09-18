package com.rg.headernotes.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rg.headernotes.databinding.FragmentSettingsBinding
import com.jakewharton.processphoenix.ProcessPhoenix
import com.rg.headernotes.viewModels.UserViewModel
import com.rg.headernotes.util.Strings
import com.rg.headernotes.util.UiState
import com.rg.headernotes.util.getAppTheme
import com.rg.headernotes.util.setAppTheme
import com.rg.headernotes.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toggleButtonDayNight.check(
            when (getAppTheme()) {
                Strings.THEME_DAY -> binding.buttonDay.id
                Strings.THEME_NIGHT -> binding.buttonNight.id
                Strings.THEME_SYSTEM -> binding.buttonSystem.id
                else -> binding.buttonSystem.id
            }
        )

        binding.toggleButtonDayNight.addOnButtonCheckedListener { toggleButton, _, _ ->
            CoroutineScope(Dispatchers.Main).launch {
                setAppTheme(
                    when (toggleButton.checkedButtonId) {
                        binding.buttonDay.id -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            Strings.THEME_DAY
                        }

                        binding.buttonNight.id -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            Strings.THEME_NIGHT
                        }

                        else -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                            Strings.THEME_SYSTEM
                        }
                    }
                )
            }
        }

        binding.buttonDeleteUser.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Удаление профиля")
                .setMessage("Вы действительно хотите удалить Ваш профиль без возможности восстановления? В случае удаления, вы потеряете все вложенные данные.")
                .setNegativeButton("Удалить") { dialog, which ->
                    viewModel.deleteUser()
                    dialog.dismiss()
                }
                .setPositiveButton("Отмена") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
        viewModel.deleteUser.observe(viewLifecycleOwner){
            when(it){
                is UiState.Success -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        ProcessPhoenix.triggerRebirth(requireContext().applicationContext)
                        showSnackbar("Профиль удалён.")
                    }
                }
                is UiState.Failure -> {
                    showSnackbar("Профиль удалён.")
                }

                else -> {

                }
            }
        }
    }
}

