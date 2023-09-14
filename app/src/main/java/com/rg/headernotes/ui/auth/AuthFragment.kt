package com.rg.headernotes.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.firebase.ui.auth.AuthUI.IdpConfig.GoogleBuilder
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.rg.headernotes.R
import com.rg.headernotes.databinding.FragmentAuthBinding
import com.rg.headernotes.ui.addUser.AddUserViewModel
import com.rg.headernotes.ui.employers.EmployerModel
import com.rg.headernotes.util.GraphActions
import com.rg.headernotes.util.Strings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding
    private var firebaseAuth: FirebaseAuth? = null
    private var authStateListener: AuthStateListener? = null
    private var signInIntent : Intent?= null

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { result ->
        if(result.resultCode !=  Activity.RESULT_OK){
            Toast.makeText(requireContext(), Strings.ERROR, Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(layoutInflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        authStateListener = AuthStateListener { firebaseAuth ->
            firebaseAuth.currentUser?.let {
                signInIntent?.let {
                    Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                        .navigate(GraphActions.authToAddUser)
                }?: run {
                    Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                        .navigate(GraphActions.authToMain)
                }
            }?: run {
                signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setAvailableProviders(listOf(GoogleBuilder().build()))
                    .setLogo(R.mipmap.ic_launcher_round).setTheme(R.style.Theme_HeaderNotes)
                    .build()
                signInLauncher.launch(signInIntent)
            }
        }
        return binding.root
    }

    override fun  onStart(){
        super.onStart()
        authStateListener?.let { stateListener ->
            firebaseAuth?.addAuthStateListener(stateListener)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        authStateListener?.let { stateListener ->
            firebaseAuth?.removeAuthStateListener(stateListener)
        }
    }
}