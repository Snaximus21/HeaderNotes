package com.rg.headernotes.util

import android.app.Activity
import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.rg.headernotes.MainActivity
import com.rg.headernotes.R

fun Fragment.showSnackbar(message: String, action: (Snackbar) -> Unit = {}) : Snackbar{
    val snackbar = Snackbar.make((host as MainActivity).binding.coordinatorLayout, message, Snackbar.LENGTH_SHORT)
    action.invoke(snackbar)
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


fun Activity.getAppTheme() : String{
    val preferences = applicationContext.getSharedPreferences("themeApp", Context.MODE_PRIVATE)
    val result = preferences.getString("themeApp", Strings.THEME_SYSTEM)
    result ?: return Strings.THEME_SYSTEM
    return result
}
fun Activity.setAppTheme(theme: String){
    val preferences = applicationContext.applicationContext.getSharedPreferences("themeApp", Context.MODE_PRIVATE)
    val outValue = when(theme){
        Strings.THEME_DAY, Strings.THEME_NIGHT, Strings.THEME_SYSTEM -> theme
        else -> Strings.THEME_SYSTEM
    }
    preferences.edit().putString("themeApp", outValue).apply()
}

fun Fragment.getAppTheme() : String{
    val preferences = requireContext().applicationContext.getSharedPreferences("themeApp", Context.MODE_PRIVATE)
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

fun Fragment.showPopupMenu(v: View, @MenuRes menuRes: Int, menuItemClickListener: (MenuItem) -> Unit = {}, onDismissListener: () -> Unit = {}) {
    PopupMenu(requireContext(), v).apply {
        menuInflater.inflate(menuRes, menu)
        setOnMenuItemClickListener { menuItem: MenuItem ->
            menuItemClickListener.invoke(menuItem)
            true
        }
        setOnDismissListener {
            onDismissListener.invoke()
        }
    }.show()
}

fun Activity.showPopupMenu(v: View, @MenuRes menuRes: Int, menuItemClickListener: (MenuItem) -> Unit = {}, onDismissListener: () -> Unit = {}) {
    PopupMenu(applicationContext, v).apply {
        menuInflater.inflate(menuRes, menu)
        setOnMenuItemClickListener { menuItem: MenuItem ->
            menuItemClickListener.invoke(menuItem)
            true
        }
        setOnDismissListener {
            onDismissListener.invoke()
        }
    }.show()
}
