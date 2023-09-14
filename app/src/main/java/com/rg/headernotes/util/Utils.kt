package com.rg.headernotes.util

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.rg.headernotes.MainActivity
import com.rg.headernotes.MainActivity_GeneratedInjector

fun Fragment.showMessage(message: String){
    Snackbar.make((host as MainActivity).binding.coordinatorLayout, message, Snackbar.LENGTH_SHORT).show()
}