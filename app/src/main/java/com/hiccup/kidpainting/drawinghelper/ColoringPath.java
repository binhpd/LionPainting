package com.hiccup.kidpainting.drawinghelper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.hiccup.kidpainting.annotation.Draw;

/**
 * Created by ${binhpd} on 6/18/2017.
 */

public class ColoringPath extends BasePath {
    private DrawingPoint lastPoint;

    private boolean animation = true;

    public ColoringPath(Context context) {
        super(context);
    }

    @Draw.PathDraw
    @SuppressWarnings("unused")
    public void doDraw(Canvas canvas) {
        if (points.size() == 0)
            return;

        if (points.size() <= 2) {
            DrawingPoint p = points.get(0);
            canvas.drawPoint(p.x, p.y, paint);
        } else {
            canvas.drawPath(path, paint);
        }
    }

    @Override
    public void addPoint(DrawingPoint point) {
        points.add(point);
        if (points.size() == 1) {
            path.moveTo(point.x, point.y);
        } else {
            if (lastPoint.action == MotionEvent.ACTION_UP) {
                path.moveTo(point.x, point.y);
            } else {
                path.quadTo(lastPoint.x, lastPoint.y, (lastPoint.x + point.x) / 2, (lastPoint.y + point.y) / 2);
            }
        }

        lastPoint = point;
    }

    public void setFinish(boolean finish) {
        addPointFinished = finish;
    }

    @Override
    public boolean finished() {
        return addPointFinished;
    }

    @Draw.PathUpdate
    @SuppressWarnings("unused")
    public void doUpdate(long dtms) {
        if (!animation) {
            return;
        }
    }
}
