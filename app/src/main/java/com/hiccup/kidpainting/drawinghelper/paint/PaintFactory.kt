package com.hiccup.kidpainting.drawinghelper.paint

import android.content.Context
import android.graphics.Paint

object PaintFactory {

    const val ERASE_PAINT = 0
    const val PENCIL_PAINT = 1
    const val MARKER_PAINT = 2
    const val BRUSH_PAINT = 3

    fun getPaint(context: Context, paintType: Int): Paint {
        when (paintType) {
            ERASE_PAINT -> {
                return EraserPaint(context)
            }
            PENCIL_PAINT -> {
                return PencilPaint(context)
            }
            MARKER_PAINT -> {
                return MarkerPaint(context)
            }
            BRUSH_PAINT -> {
                return BrushPaint(context)
            }
        }
        return BrushPaint(context)
    }

    fun getTypePaint(paint: Paint): Int {
        return when (paint) {
            is EraserPaint -> ERASE_PAINT
            is PencilPaint -> PENCIL_PAINT
            is MarkerPaint -> MARKER_PAINT
            is BrushPaint -> BRUSH_PAINT
            else -> ERASE_PAINT
        }
    }
}
