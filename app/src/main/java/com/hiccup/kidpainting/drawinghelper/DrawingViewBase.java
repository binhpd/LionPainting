package com.hiccup.kidpainting.drawinghelper;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.hiccup.kidpainting.common.KidConfig;
import com.hiccup.kidpainting.drawinghelper.paint.PaintFactory;


/**
 * Created by hiepnd on Feb 29, 2016.
 */
public abstract class DrawingViewBase extends View implements IDrawingView {
    public static final int NORMAL_MODE = 0;
    public static final int PREVIEW_MODE = 1;
    public static final int BRUSH_MODE = 2;
    protected int mode = BRUSH_MODE;

    protected DrawHandler mHandler;

    public DrawingViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Init my paint
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (mode) {
            case PREVIEW_MODE:
                //do thing now
                break;
            case NORMAL_MODE:
                drawMyPoint(new DrawingPoint(0, event.getAction(), event.getX(), event.getY()));
                break;

            case BRUSH_MODE:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    onDoFloodFill(new DrawingPoint(0, event.getAction(), event.getX(), event.getY()));
                }
                break;
        }
        return true;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setDrawHandler(DrawHandler handler) {
        this.mHandler = handler;
    }
}
