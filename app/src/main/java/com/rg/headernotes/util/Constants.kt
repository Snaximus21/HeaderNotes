package com.rg.headernotes.util

import com.rg.headernotes.R


object GraphActions {
    val startToAuth: Int = R.id.actionStartFragmentToAuthFragment
    val startToVpStart: Int = R.id.actionStartFragmentToViewPagerStartFragment
    val authToAddUser: Int = R.id.actionAuthFragmentToAddUserFragment
    val authToMain: Int = R.id.actionAuthFragmentToMainFragment
    val addUserToMain: Int = R.id.actionAddUserFragmentToMainFragment
    val vpStartToVpAuth: Int = R.id.actionViewPagerStartFragmentToAuthFragment
}

object Identifiers {
    val navHostFragment = R.id.navHostFragment
    val viewPagerStart = R.id.viewPagerStart
}

object FireStoreTables {
    const val USER = "users"
    const val EMPLOYERS = "employers"
    const val NOTES = "notes"
    const val TASKS = "tasks"
}

object Strings {
    val CREATED = "${R.string.created}"
    val DELETED = "${R.string.deleted}"
    val ADDED = "${R.string.added}"
    val UPDATED = "${R.string.updated}"
    val ERROR = "${R.string.error}"

    val THEME_DAY = "${R.string.theme_day}"
    val THEME_NIGHT = "${R.string.theme_night}"
    val THEME_SYSTEM = "${R.string.theme_system}"
}

object MenuItems{
    val NOTES = R.id.navNotes
    val TASKS = R.id.navTasks
    val EMPLOYERS = R.id.navEmployers
    val SETTINGS = R.id.navSettings
}

object RequestCodes {
    const val newTask = "newTask"
    const val editTask = "editTask"
    const val setNote = "setNote"
    const val newNote = "newNote"
    const val editNote = "editNote"
}