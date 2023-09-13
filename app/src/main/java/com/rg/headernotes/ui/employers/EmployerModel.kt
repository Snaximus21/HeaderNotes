package com.rg.headernotes.ui.employers

data class EmployerModel(
    val id: Int = 0,
    val fullName: String = "",
    val job: String = "",
    val age: Int = 0,
    val notesCount: Int = 0,
    val tasksCount: Int = 0
)