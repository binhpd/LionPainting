package com.hiccup.kidpainting.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

class KidTextView(context: Context?, attrs: AttributeSet?) : TextView(context, attrs) {
    init {
        if (context != null) {
            typeface = Typeface.createFromAsset(context.assets, "fonts/iCielCadena.otf")
        }
    }
}
