package com.rg.headernotes.ui.employers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class DeviderDecorator : ItemDecoration() {
    private val bounds = Rect()
    private val paint = Paint().apply{
        color = Color.GRAY
        strokeWidth = 3f
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        for(position in 0 until parent.childCount){
            val child = parent.getChildAt(position)
            parent.getDecoratedBoundsWithMargins(child, bounds)

            val currPos = parent.getChildAdapterPosition(child)
            val lastElementPos = parent.adapter?.itemCount
            if(currPos != lastElementPos){
                c.drawLine(210f, bounds.top.toFloat(), parent.width.toFloat(), bounds.top.toFloat(), paint)
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.apply {
            left = 0
            right = 0
            top = 5
            bottom = 5
        }
    }
}