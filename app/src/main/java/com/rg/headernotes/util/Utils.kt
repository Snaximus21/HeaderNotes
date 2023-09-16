package com.rg.headernotes.util

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.rg.headernotes.MainActivity
import com.rg.headernotes.R

fun Fragment.showSnackbar(message: String, action: (Snackbar) -> Unit = {}) : Snackbar{
    val snackbar = Snackbar.make((host as MainActivity).binding.coordinatorLayout, message, Snackbar.LENGTH_SHORT)
    action(snackbar)
    snackbar.show()
    return snackbar
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
    val result = preferences.getString("themeApp", Strings.THEME_SYSTEM)
    result ?: return Strings.THEME_SYSTEM
    return result
}

fun Activity.getAppTheme() : String{
    val preferences = applicationContext.getSharedPreferences("themeApp", Context.MODE_PRIVATE)
    val result = preferences.getString("themeApp", Strings.THEME_SYSTEM)
    result ?: return Strings.THEME_SYSTEM
    return result
}
fun Fragment.setAppTheme(theme: String){
    val preferences = requireContext().applicationContext.getSharedPreferences("themeApp", Context.MODE_PRIVATE)
    val outValue = when(theme){
        Strings.THEME_DAY, Strings.THEME_NIGHT, Strings.THEME_SYSTEM -> theme
        else -> Strings.THEME_SYSTEM
    }
    preferences.edit().putString("themeApp", outValue).apply()
}
