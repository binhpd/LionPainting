package com.hiccup.kidpainting.training.guideDrawing;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;

import com.hiccup.kidpainting.core.Updatable;
import com.hiccup.kidpainting.drawinghelper.DrawingPoint;
import com.hiccup.kidpainting.view.ColoringView;

/**
 * Created by ${binhpd} on 9/17/2016.
 */
public class GuideDrawingSequence {

    private static final int NORMAL_TIME_CALL = 30;
    private static final long TIME_OUT_ADD_STICKER = 3000;
    private int viewWidth;
    private int viewHeight;

    private ArrayList<DrawingPoint> coloringPoints
            ;
    // The point to be sent next
    private Updatable updatable;

    private ColoringView drawingViewBase;

    public GuideDrawingSequence(Context context, ColoringView drawingViewBase) {
        this.drawingViewBase = drawingViewBase;
    }


    public void startReadData() {
        if (updatable != null && updatable.isRunning()) {
            return ;
        }

        updatable = new Updatable() {
            @Override
            public void update(long deltaTimeMs) {
                updateMySticker(deltaTimeMs);
            }
        };
        updatable.start();
    }

    private void updateMySticker(long dt) {
        Iterator<DrawingPoint> coloringPointIterator = coloringPoints.iterator();
        while (coloringPointIterator.hasNext()) {
            DrawingPoint coloringPoint = coloringPointIterator.next();
            int countPointsAdded = coloringPoints.size() / NORMAL_TIME_CALL;
            if (countPointsAdded == 0) {
                countPointsAdded = coloringPoints.size();
            }
            for (int i = 0; i < countPointsAdded; i++) {
                drawingViewBase.onDrawGuidePath(coloringPoint);
            }
        }
    }
}
