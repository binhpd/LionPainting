package com.hiccup.kidpainting.drawinghelper.painter;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hiccup.kidpainting.drawinghelper.DrawingPoint;
import com.hiccup.kidpainting.drawinghelper.IDrawingView;

/**
 * Created by ${binhpd} on 3/26/2016.
 */
public abstract class DrawingPainter {
    protected static final String TAG = DrawingPainter.class.getSimpleName();

    protected final List<DrawingPoint> drawingPoints = new ArrayList<>();
    protected IDrawingView mDrawingView;

    public DrawingPainter(IDrawingView view) {
        mDrawingView = view;
    }

    public abstract void draw(List<DrawingPoint> nhoPoints);

    /**
     * Draw a line to point with its action to view
     */
    protected void drawPointToView(DrawingPoint drawingPoint) {
        mDrawingView.drawMyPoint(drawingPoint);
        Glide.with(new Fragment())
                .asBitmap()
                .load("fsdfsfsf")
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    }
                });
    }

}

