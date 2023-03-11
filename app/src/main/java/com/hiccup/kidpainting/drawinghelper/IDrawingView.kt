package com.hiccup.kidpainting.drawinghelper

/**
 * Created by ${binhpd} on 3/26/2016.
 */
interface IDrawingView {
    fun drawMyPoint(point: DrawingPoint)

    fun onDoFloodFill(point: DrawingPoint)

    fun clear()
}
