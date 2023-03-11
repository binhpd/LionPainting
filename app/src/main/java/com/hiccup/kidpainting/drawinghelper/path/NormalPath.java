package com.hiccup.kidpainting.drawinghelper.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.view.MotionEvent;

import com.hiccup.kidpainting.annotation.Draw;
import com.hiccup.kidpainting.drawinghelper.BasePath;
import com.hiccup.kidpainting.drawinghelper.DrawingPoint;

public class NormalPath extends BasePath {
    private DrawingPoint lastPoint;

    public NormalPath(Context context) {
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
                paint.reset();
            } else {
                path.quadTo(lastPoint.x, lastPoint.y, (lastPoint.x + point.x) / 2, (lastPoint.y + point.y) / 2);
            }
        }

        lastPoint = point;
    }

    @Draw.PathUpdate
    @SuppressWarnings("unused")
    public void doUpdate(long dtms) {
    }

    @Override
    public boolean finished() {
        return points.size() >= 2;
    }

}
