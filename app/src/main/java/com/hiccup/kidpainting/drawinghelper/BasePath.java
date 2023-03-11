package com.hiccup.kidpainting.drawinghelper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

import java.lang.reflect.Method;
import java.util.ArrayList;

import com.hiccup.kidpainting.annotation.Draw;
import com.hiccup.kidpainting.common.KidConfig;
import com.hiccup.kidpainting.drawinghelper.paint.PaintFactory;
import com.hiccup.kidpainting.utilities.Assert;

/**
 * Created by ${binhpd} on 3/26/2016.
 */
public abstract class BasePath {
    protected Path path;
    protected boolean addPointFinished;
    protected ArrayList<DrawingPoint> points = new ArrayList<>();
    Method updateMethod;
    Method drawMethod;

    protected Paint paint;

    public BasePath(Context context) {
        super();
        paint = PaintFactory.INSTANCE.getPaint(context, KidConfig.DEFAULT_PAINT);
        path = new Path();

        Class cls = this.getClass();
        for (Method method : cls.getMethods()) {
            // Update method is annotated with @Draw.PathUpdate
            Draw.PathUpdate updateAnnotation = method.getAnnotation(Draw.PathUpdate.class);
            if (updateAnnotation != null) {
                Assert.assertTrue(updateMethod == null, "Duplicate Draw.PathUpdate");
                updateMethod = method;
            }

            // Draw method is annotated with @Draw.PathDraw
            Draw.PathDraw drawAnnotation = method.getAnnotation(Draw.PathDraw.class);
            if (drawAnnotation != null) {
                Assert.assertTrue(drawMethod == null, "Duplicate Draw.PathDraw");
                drawMethod = method;
            }
        }
    }

    public void addPoint(DrawingPoint point) {
        points.add(point);
        addPointFinished = point.action == MotionEvent.ACTION_UP || point.action == MotionEvent.ACTION_CANCEL;
    }

    public ArrayList<DrawingPoint> getPoints() {
        return points;
    }

    public boolean isAddPointFinished() {
        return addPointFinished;
    }

    /**
     * Update the path
     *
     * @param dtms time since the last update in millisecond
     */
    public final void update(long dtms) {
        try {
            updateMethod.invoke(this, new Object[]{dtms});
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false, "Fix me");
        }
    }

    public final void draw(Canvas canvas) {
        try {
            drawMethod.invoke(this, new Object[]{canvas});
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false, "Fix me");
        }
    }

    public void clear() {
        if (points.size() > 0) {
            DrawingPoint p = points.get(0);
            points.clear();
            if (p.action == MotionEvent.ACTION_MOVE || p.action == MotionEvent.ACTION_DOWN) {
                points.add(p);
            }
        }
    }

    public abstract boolean finished();

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public Paint getPaint() {
        return this.paint;
    }

    public int getColor(){
        return paint.getColor();
    }
}
