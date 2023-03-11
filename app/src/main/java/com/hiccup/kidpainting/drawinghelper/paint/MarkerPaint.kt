package com.hiccup.kidpainting.drawinghelper.paint

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import com.hiccup.kidpainting.R

class MarkerPaint(context: Context) : Paint() {
    init {
        color = Color.BLACK
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        val brushSize = context.resources.getDimensionPixelSize(R.dimen.brush_size_4)
        strokeWidth = brushSize.toFloat()
    }
}
