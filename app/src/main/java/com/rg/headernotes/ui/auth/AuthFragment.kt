package com.rg.headernotes.ui.auth

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.firebase.ui.auth.AuthUI.IdpConfig.GoogleBuilder
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.rg.headernotes.R
import com.rg.headernotes.databinding.FragmentAuthBinding
import com.rg.headernotes.util.GraphActions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding
    private var firebaseAuth: FirebaseAuth? = null
    private var authStateListener: AuthStateListener? = null

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { result ->
        if(result.resultCode ==  Activity.RESULT_OK){
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {

            }
            Toast.makeText(requireContext(), "OK", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(requireContext(), "NE OK", Toast.LENGTH_SHORT).show()
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
                Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                    .navigate(GraphActions.authToMain)
            }?: run {
                /*val signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setAvailableProviders(listOf(GoogleBuilder().build()))
                    .setLogo(R.mipmap.ic_launcher_round).setTheme(R.style.Theme_HeaderNotes)
                    .build()
                signInLauncher.launch(signInIntent)*/
                Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                    .navigate(GraphActions.authToMain)
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