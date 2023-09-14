package com.rg.headernotes.util

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.rg.headernotes.MainActivity
import com.rg.headernotes.MainActivity_GeneratedInjector
import com.rg.headernotes.R

fun Fragment.showMessage(message: String){
    Snackbar.make((host as MainActivity).binding.coordinatorLayout, message, Snackbar.LENGTH_SHORT).show()
}

fun Fragment.navigate(route: Int){
    Navigation.findNavController(
        requireActivity(),
        R.id.navHostFragment
    ).navigate(route)
}