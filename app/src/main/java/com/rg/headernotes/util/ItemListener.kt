package com.rg.headernotes.util

import android.view.View

interface ItemListener {
    fun onItemClickListener(position: Int)
    fun onLongItemClickListener(view: View, position: Int)
}