package com.rg.headernotes.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.rg.headernotes.MainActivity
import com.rg.headernotes.R
import com.rg.headernotes.databinding.FragmentMainBinding
import com.rg.headernotes.databinding.HeaderNavigationBinding
import com.rg.headernotes.ui.auth.AuthViewModel
import com.rg.headernotes.ui.employers.EmployersFragment
import com.rg.headernotes.ui.notes.NotesFragment
import com.rg.headernotes.util.MenuItems
import com.rg.headernotes.util.Strings
import com.rg.headernotes.util.UiState
import com.rg.headernotes.util.showMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val binding by lazy { FragmentMainBinding.inflate(layoutInflater) }
    private val viewModelAuth by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    childFragmentManager.apply {
                        when {
                            backStackEntryCount > 0 -> {
                                requireActivity().finish()
                            }
                            else -> {
                                isEnabled = false
                                showMessage("Нажмите еще раз для выхода")
                            }
                        }
                    }
                }
            }
        )

        //Аттачим граф навигации к панели навигации
        val navHost = childFragmentManager.findFragmentById(R.id.navHostFragmentContentMain) as NavHostFragment
        binding.navigationView.setupWithNavController(navHost.navController)

        //Аттачим хидер к панели навигации
        val headerBinding = HeaderNavigationBinding.inflate(layoutInflater)
        binding.navigationView.addHeaderView(headerBinding.root)

        //Грузим данные пользователя для заполнения хидера
        viewModelAuth.getUser()
        viewModelAuth.user.observe(viewLifecycleOwner){
            when (it) {
                is UiState.Success -> {
                    headerBinding.textViewName.text = it.data.name
                    headerBinding.textViewSubdivision.text = it.data.subDivision
                }

                is UiState.Loading -> {

                }

                else -> {
                    headerBinding.textViewName.text = Strings.ERROR
                    headerBinding.textViewSubdivision.text = Strings.ERROR
                }
            }
        }

        headerBinding.buttonSettings.setOnClickListener {
            showMessage("UserClicked")
            binding.drawerLayout.close()
        }
    }
}