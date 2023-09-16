package com.rg.headernotes.util

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
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

fun String.isElementNull() : Boolean{
    return this.trim().replace("null", "").isEmpty()
}

fun Fragment.getAppTheme() : String{
    val preferences = requireContext().applicationContext.getSharedPreferences("themeApp", Context.MODE_PRIVATE)
    val result = preferences.getString("themeApp", "Системная")
    result ?: return "Системная"
    return result
}

fun Activity.getAppTheme() : String{
    val preferences = applicationContext.getSharedPreferences("themeApp", Context.MODE_PRIVATE)
    val result = preferences.getString("themeApp", "Системная")
    result ?: return "Системная"
    return result
}
fun Fragment.setAppTheme(theme: String){
    val preferences = requireContext().applicationContext.getSharedPreferences("themeApp", Context.MODE_PRIVATE)
    val outValue = when(theme){
        "Светлая", "Темная", "Системная" -> theme
        else -> "Системная"
    }
    preferences.edit().putString("themeApp", outValue).apply()
}
