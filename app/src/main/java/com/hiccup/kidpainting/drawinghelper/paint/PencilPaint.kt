package com.hiccup.kidpainting.drawinghelper.paint

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import com.hiccup.kidpainting.R

class PencilPaint(context: Context) : Paint() {
    init {
        color = Color.GREEN
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        val brushSize = context.resources.getDimensionPixelSize(R.dimen.brush_size_1)
        strokeWidth = brushSize.toFloat()
    }
}