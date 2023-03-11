package com.hiccup.kidpainting.drawinghelper

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import com.hiccup.kidpainting.common.KidConfig
import com.hiccup.kidpainting.drawinghelper.paint.EraserPaint
import com.hiccup.kidpainting.drawinghelper.paint.PaintFactory
import com.hiccup.kidpainting.drawinghelper.path.PathFactory
import com.hiccup.kidpainting.utilities.Assert
import java.util.*


class UserDraw(val context: Context) {
    var paint: Paint = PaintFactory.getPaint(context, KidConfig.DEFAULT_PAINT)
    var currentPath: BasePath? = null
    var paths = ArrayList<BasePath>()

    //canvas
    private var cacheCanvas: Canvas? = null
    //canvas bitmap
    private var cacheBitmap: Bitmap? = null
    // original bitmap
    // canvas paint
    private var mPaint: Paint? = null
    private var h: Int = 0
    private var w: Int = 0
    var coloringBitmap: Bitmap? = null

    var originBitmap: Bitmap? = null


    init {
        mPaint = Paint(Paint.DITHER_FLAG)
    }

    fun initBitmap(w: Int, h: Int) {
        this.w = w
        this.h = h
        if (originBitmap != null) {
            coloringBitmap = scaleImage(w, h, originBitmap!!)
        }

        if (paths.size == 0) {
            cacheBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            cacheCanvas = Canvas(cacheBitmap!!)
        }
    }

    fun forceInitCacheBitmap(w: Int, h: Int) {
        this.w = w
        this.h = h

        cacheBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        cacheCanvas = Canvas(cacheBitmap!!)
    }

    fun isInit(): Boolean {
        return cacheCanvas != null
    }

    fun onDraw(canvas: Canvas) {
        // draw cache bitmap
        if (mPaint == null) {
            return
        }

        // for coloring
        if (coloringBitmap != null) {
            canvas.drawBitmap(coloringBitmap!!, 0f, 0f, mPaint!!)
        }

        // for previous path
        canvas.drawBitmap(cacheBitmap!!, 0f, 0f, mPaint!!)

        // draw last path
        if (paths.size > 0) {
            paths[paths.size - 1].draw(canvas)
        }
    }

    fun redraw(deltatimeMs: Long) {
        val iterator = paths.iterator()
        while (iterator.hasNext()) {
            val p = iterator.next()
            p.update(deltatimeMs)
            p.draw(cacheCanvas)
        }
    }

    fun drawPoint(point: DrawingPoint) {
        // don't add erase path when user have not drawing anything
        if (paint is EraserPaint && paths.size == 0) {
            return
        }

        if (point.action == MotionEvent.ACTION_DOWN) {
            generateCurrentPath()
            paths.add(currentPath!!)
        }

        Assert.assertTrue(currentPath != null, "currentPath must not be NULL")
        currentPath!!.addPoint(point)

        if (point.action == MotionEvent.ACTION_UP || point.action == MotionEvent.ACTION_CANCEL) {
            currentPath!!.draw(cacheCanvas)
            currentPath = null
        }
    }


    private fun generateCurrentPath() {
        currentPath = PathFactory.getPath(context, PathFactory.NORMAL_PATH)
        currentPath?.setPaint(paint)
    }

    fun clear() {
        paths.clear()
        currentPath = null

        cacheBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        cacheCanvas = Canvas(cacheBitmap!!)
    }

    private fun scaleImage(width: Int, height: Int, originalImage: Bitmap): Bitmap {
        val background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val originalWidth = originalImage.width
        val originalHeight = originalImage.height

        val canvas = Canvas(background)

        val scaleY = height.toFloat() / originalHeight.toFloat()
        val scaleX = width.toFloat() / originalWidth.toFloat()

        val scale = if (scaleX > scaleY) {
            scaleY
        } else {
            scaleX
        }
        val xTranslation = (width - originalWidth * scale) / 2.0f
        val yTranslation = (height - originalHeight * scale) / 2.0f

        val transformation = Matrix()
        transformation.postTranslate(xTranslation, yTranslation)
        transformation.preScale(scale, scale)

        val paint = Paint()
        paint.isFilterBitmap = true
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(originalImage, transformation, paint)

        return background
    }

}
