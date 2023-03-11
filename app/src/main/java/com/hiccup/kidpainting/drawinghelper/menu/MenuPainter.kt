package com.hiccup.kidpainting.drawinghelper.menu

import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import android.view.View
import android.view.animation.TranslateAnimation
import com.hiccup.kidpainting.R
import com.hiccup.kidpainting.common.KidConfig
import com.hiccup.kidpainting.drawinghelper.paint.PaintFactory
import com.hiccup.kidpainting.utilities.SoundEffect

class MenuPainter(view: View): View.OnClickListener {

    private var menuPainter: MenuPainterImpl? = null
    private var mOpenedView: View? = null
    private var lastPaintType = KidConfig.DEFAULT_PAINT

    init {
        openMenu(view.findViewById(R.id.ivMarker))
        view.findViewById<View>(R.id.ivPaintBrush).setOnClickListener(this)
        view.findViewById<View>(R.id.ivMarker).setOnClickListener(this)
        view.findViewById<View>(R.id.ivPencil).setOnClickListener(this)
        view.findViewById<View>(R.id.ivErase).setOnClickListener(this)
    }

    fun setListener(menuPainter: MenuPainterImpl) {
        this.menuPainter = menuPainter
    }

    fun getCurrentPaintType(): Int {
        return lastPaintType
    }

    override fun onClick(view: View) {
        var brush = KidConfig.DEFAULT_PAINT
        when (view.id) {
            R.id.ivPaintBrush -> brush = PaintFactory.BRUSH_PAINT
            R.id.ivMarker -> brush = PaintFactory.MARKER_PAINT
            R.id.ivPencil -> brush = PaintFactory.PENCIL_PAINT
            R.id.ivErase -> brush = PaintFactory.ERASE_PAINT
        }
        menuPainter!!.onBrushChange(brush)
        if (lastPaintType != brush) {
            toggleMenu(view, mOpenedView)
            lastPaintType = brush
        }
        SoundEffect.instance.playSound(R.raw.tap)
    }

    private fun toggleMenu(view: View, mOpenedView: View?) {
        closeMenu(mOpenedView!!)
        openMenu(view)
    }

    private fun openMenu(view: View) {
        val distance = view.context.resources.getDimensionPixelOffset(R.dimen.distance_transfer_meu)
        val animationOpen = TranslateAnimation(0f, (-distance).toFloat(), 0f, (-distance).toFloat())
        animationOpen.interpolator = FastOutSlowInInterpolator()
        animationOpen.fillAfter = true
        animationOpen.duration = 250
        view.startAnimation(animationOpen)
        mOpenedView = view

    }


    private fun closeMenu(view: View) {
        val distance = view.context.resources.getDimensionPixelOffset(R.dimen.distance_transfer_meu)
        val animationClose = TranslateAnimation((-distance).toFloat(), 0f, (-distance).toFloat(), 0f)
        animationClose.interpolator = FastOutSlowInInterpolator()
        animationClose.fillAfter = true
        animationClose.duration = 250
        view.startAnimation(animationClose)
    }
}
