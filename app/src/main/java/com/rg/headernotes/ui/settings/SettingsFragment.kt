package com.rg.headernotes.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.rg.headernotes.databinding.FragmentSettingsBinding
import com.jakewharton.processphoenix.ProcessPhoenix
import com.rg.headernotes.di.HeaderNotesApp
import com.rg.headernotes.util.getAppTheme
import com.rg.headernotes.util.setAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private val binding by lazy { FragmentSettingsBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = requireContext().applicationContext.getSharedPreferences(
            "themeApp",
            Context.MODE_PRIVATE
        )
        binding.toggleButtonDayNight.check(
            when (getAppTheme()) {
                "Системная", "System" -> binding.buttonSystem.id
                "Темная", "Dark" -> binding.buttonNight.id
                "Светлая", "Light" -> binding.buttonDay.id
                else -> binding.buttonSystem.id
            }
        )

        binding.toggleButtonDayNight.addOnButtonCheckedListener { toggleButton, _, _ ->
            CoroutineScope(Dispatchers.Main).launch {
                setAppTheme(
                    when (toggleButton.checkedButtonId) {
                        binding.buttonDay.id -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            "Светлая"
                        }

                        binding.buttonNight.id -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            "Темная"
                        }

                        else -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                            "Системная"
                        }
                    }
                )
            }
        }
    }
}

