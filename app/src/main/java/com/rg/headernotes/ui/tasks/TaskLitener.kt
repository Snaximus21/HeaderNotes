package com.rg.headernotes.ui.tasks

interface TaskLitener {
    fun onButtonDoneClicked(position: Int)
    fun onButtonPostponeClicked(position: Int)
}