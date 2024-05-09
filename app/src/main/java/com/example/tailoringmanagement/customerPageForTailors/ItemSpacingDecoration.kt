package com.example.tailoringmanagement.customerPageForTailors

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemSpacingDecoration(private val l: Int, private val r: Int, private val t: Int, private val b: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = t
        outRect.bottom = b
        outRect.left = l
        outRect.right = r
    }
}