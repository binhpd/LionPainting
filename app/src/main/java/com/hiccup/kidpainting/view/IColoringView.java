package com.hiccup.kidpainting.view;

import android.graphics.Paint;

import com.hiccup.kidpainting.drawinghelper.DrawingPoint;

/**
 * Created by ${binhpd} on 4/18/2016.
 */
public interface IColoringView {
    void createNewPaint(Paint paint);

    void onDrawGuidePath(DrawingPoint coloringPoint);

    void addPoint(DrawingPoint addPoint);

    void finishCurrentPath();

    void removeTopGuidePath();
}
