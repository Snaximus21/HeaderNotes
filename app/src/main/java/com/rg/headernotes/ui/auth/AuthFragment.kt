package com.rg.headernotes.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.firebase.ui.auth.AuthUI.IdpConfig.GoogleBuilder
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.rg.headernotes.R
import com.rg.headernotes.databinding.FragmentAuthBinding
import com.rg.headernotes.util.GraphActions
import com.rg.headernotes.util.UiState
import com.rg.headernotes.util.isElementNull
import com.rg.headernotes.util.navigate
import com.rg.headernotes.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding
    private var firebaseAuth: FirebaseAuth? = null
    private var authStateListener: AuthStateListener? = null
    private var signInIntent: Intent? = null
    private val viewModel by viewModels<AuthViewModel>()

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { result ->
        showSnackbar(
            if (result.resultCode != Activity.RESULT_OK)
                "Ошибка при выполнении авторизации."
            else
                "Авторизация прошла успешно."
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        authStateListener = AuthStateListener { firebaseAuth ->
            firebaseAuth.currentUser?.let {
                viewModel.getUser()
                viewModel.user.observe(viewLifecycleOwner) {
                    when (it) {
                        is UiState.Loading -> {
                            showSnackbar("Загрузка профиля...")
                        }

                        is UiState.Success -> {
                            if (it.data.name.isElementNull() or it.data.name.isElementNull()) {
                                navigate(GraphActions.authToAddUser)
                            } else {
                                navigate(GraphActions.authToMain)
                                showSnackbar("Приветствую ${it.data.name}")
                            }
                        }

                        is UiState.Failure -> {
                            showSnackbar("Ошибка при загрузке данных пользователя.")
                        }
                    }
                }
            } ?: run {
                signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setAvailableProviders(listOf(GoogleBuilder().build()))
                    .setLogo(R.mipmap.ic_launcher_round).setTheme(R.style.Theme_HeaderNotes)
                    .build()
                signInLauncher.launch(signInIntent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        authStateListener?.let { stateListener ->
            firebaseAuth?.addAuthStateListener(stateListener)
        }
    }

    override fun onPause() {
        super.onPause()
        authStateListener?.let { stateListener ->
            firebaseAuth?.removeAuthStateListener(stateListener)
        }
    }
}