package com.hiccup.kidpainting.drawinghelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;

import com.hiccup.kidpainting.drawinghelper.floodfill.QueueLinearFloodFiller;
import com.hiccup.kidpainting.view.IColoringView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Drawing view for BasePath
 * Created by hiepnd on 2/25/16.
 */
public class DrawingView extends DrawingViewBase implements IColoringView {
    private long lastTickMs;
    private long deltatimeMs;
    private UserDraw myDraw;
    private UserDraw replayDraw;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        lastTickMs = SystemClock.elapsedRealtime();
        myDraw = new UserDraw(context);
        replayDraw = new UserDraw(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        myDraw.initBitmap(w, h);
        if (!replayDraw.isInit()) {
            replayDraw.initBitmap(w, h);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        deltatimeMs = SystemClock.elapsedRealtime() - lastTickMs;
        lastTickMs += deltatimeMs;
        if (mode == PREVIEW_MODE) {
            replayDraw.onDraw(canvas);
        } else {
            myDraw.onDraw(canvas);
        }
        invalidate();
    }

    @Override
    public void drawMyPoint(DrawingPoint point) {
        myDraw.drawPoint(point);
        invalidate();
    }

    @Override
    public void clear() {
        if (mode == NORMAL_MODE) {
            myDraw.clear();
        } else {
            replayDraw.clear();
        }
        invalidate();
    }

    public void setAllPaths(List<BasePath> paths) {
        myDraw.setPaths((ArrayList<BasePath>) paths);
    }

    public void refresh() {
        redrawAllPaths();
        invalidate();
    }

    public void undo() {
        ArrayList<BasePath> paths = myDraw.getPaths();
        if (paths.size() > 0) {
            paths.remove(paths.size() - 1);
        }
        redrawAllPaths();
        invalidate();
    }

    public void setPaint(Paint paint) {
        if (mode == PREVIEW_MODE) {
            replayDraw.setPaint(paint);
        } else {
            myDraw.setPaint(paint);
        }
    }

    public Paint getPaint() {
        return myDraw.getPaint();
    }

    public List<BasePath> getAllPath() {
        return myDraw.getPaths();
    }

    @Override
    public void createNewPaint(Paint paint) {
        setPaint(paint);
    }

    @Override
    public void onDrawGuidePath(DrawingPoint coloringPoint) {
        replayDraw.drawPoint(coloringPoint);
    }

    @Override
    public void addPoint(DrawingPoint addPoint) {
    }

    @Override
    public void finishCurrentPath() {
    }

    @Override
    public void removeTopGuidePath() {

    }

    private void redrawAllPaths() {
        myDraw.forceInitCacheBitmap(getWidth(), getHeight());
        myDraw.redraw(deltatimeMs);
    }

    @Override
    public void onDoFloodFill(@NotNull DrawingPoint point) {
        int fromColor = myDraw.getColoringBitmap().getPixel((int) point.x, (int) point.y);
        int red = Color.red(fromColor);
        int green = Color.green(fromColor);
        int blue = Color.blue(fromColor);

        if (red == Color.red(Color.BLACK) && green == Color.green(Color.BLACK) && blue == Color.blue(Color.BLACK)) {
            return;
        }

        QueueLinearFloodFiller ff = new QueueLinearFloodFiller(myDraw.getColoringBitmap(), fromColor, myDraw.getPaint().getColor());
        ff.setTolerance(1);
        Bitmap filledBitmap = ff.floodFill((int) point.x, (int) point.y);
        if (filledBitmap != null) {
            setOriginBitmap(filledBitmap);
        }
    }

    public void setOriginBitmap(@Nullable Bitmap resources) {
        myDraw.setOriginBitmap(resources);
        myDraw.initBitmap(getWidth(), getHeight());
        myDraw.redraw(deltatimeMs);
    }
}
