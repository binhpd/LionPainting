package com.hiccup.kidpainting.drawinghelper.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;

import com.hiccup.kidpainting.annotation.Draw;
import com.hiccup.kidpainting.drawinghelper.BasePath;
import com.hiccup.kidpainting.drawinghelper.DrawingPoint;

/**
 * Created by ${binhpd} on 3/27/2016.
 */
public class FadeOutPath extends BasePath {
    private ArrayList<Integer> opacities = new ArrayList<>();
    private long disappearIntervalMs = 2000;

    public FadeOutPath(Context context) {
        super(context);
    }

    @Override
    public void addPoint(DrawingPoint point) {
        super.addPoint(point);
        opacities.add(255);
    }

    @Draw.PathDraw
    @SuppressWarnings("unused")
    public void doDraw(Canvas canvas, Paint paint) {
        if (points.size() < 2)
            return;

        // Cap.BUTT to avoid overdraw
        paint.setStrokeCap(Paint.Cap.BUTT);

        DrawingPoint p0;
        DrawingPoint p1;
        DrawingPoint p2;
        Path lastPath = null;
        for (int i = 2; i < points.size(); i++) {
            paint.setAlpha(opacities.get(i - 2));

            path.reset();

            p0 = points.get(i-2);
            p1 = points.get(i-1);
            p2 = points.get(i);

            DrawingPoint p = p0.add(p1);
            p.mulBy(0.5f);
            DrawingPoint pp = p1.add(p2);
            pp.mulBy(0.5f);

            path.moveTo(p.x, p.y);
            path.quadTo(p1.x, p1.y, pp.x, pp.y);

            canvas.drawPath(path, paint);
        }
    }

    @Draw.PathUpdate
    @SuppressWarnings("unused")
    public void doUpdate(long dtms) {
        for (int i = 0; i < opacities.size(); i++) {
            Integer iv = opacities.get(i);
            iv = Math.max(0, iv - (int)(((float)dtms/disappearIntervalMs) * 255));
            opacities.set(i, iv);
        }
    }

    @Override
    public boolean finished() {
        return points.size() >= 2 && opacities.get(opacities.size()-1) == 0;
    }

    @Override
    public void clear() {
        super.clear();

        if (points.size() > 0) {
            int o = opacities.get(opacities.size()-1);
            opacities.clear();
            opacities.add(o);
        } else {
            opacities.clear();
        }

    }
}

