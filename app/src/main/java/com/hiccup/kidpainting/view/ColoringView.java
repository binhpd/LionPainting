package com.hiccup.kidpainting.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.hiccup.kidpainting.R;
import com.hiccup.kidpainting.drawinghelper.ColoringPath;
import com.hiccup.kidpainting.drawinghelper.DrawingPoint;
import com.hiccup.kidpainting.models.LineColoringModel;

import java.util.ArrayList;

/**
 * Created by ${binhpd} on 4/13/2016.
 */
public class ColoringView extends DrawingViewSample implements IColoringView {

    private ArrayList<ColoringPath> mColoringPaths = new ArrayList<>();

    private LineColoringModel mLineColoringModel;
    private Paint mPaintDot;
    private ColoringPath mCurrentColoringPath;
    private int mCurrentColor;

    public ColoringView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaintDot = new Paint();
        mPaintDot.setColor(context.getResources().getColor(R.color.main_yellow));
        mPaintDot.setStrokeWidth(context.getResources().getDimensionPixelSize(R.dimen.paint_line_size_width));
        mPaintDot.setStrokeJoin(Paint.Join.ROUND);
        mPaintDot.setStrokeCap(Paint.Cap.ROUND);
        mPaintDot.setStyle(Paint.Style.STROKE);
        mPaintDot.setAlpha(100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPathGuide(canvas);
        super.onDraw(canvas);
    }

    /**
     * draw path guide
     */
    private void drawPathGuide(Canvas canvas) {
        for (int i = 0; i < mColoringPaths.size(); i++) {
            mColoringPaths.get(i).draw(canvas);
        }
    }

    /**
     * draw path with dot style
     * @param lineColoringModel line data
     */
    private void drawLineDot(Canvas canvas, LineColoringModel lineColoringModel) {
        for (DrawingPoint coloringPoint : lineColoringModel.getDrawingPoints()) {
            mPaintDot.setColor(lineColoringModel.getColor());
            mPaintDot.setAlpha(100);
            canvas.drawPoint(coloringPoint.x * mWidth, coloringPoint.y * height, mPaintDot);
        }
    }


    @Override
    public void createNewPaint(Paint paint) {
        mCurrentColor = paint.getColor();
    }

    @Override
    public void onDrawGuidePath(DrawingPoint coloringPoint) {
        if (coloringPoint.action == MotionEvent.ACTION_UP) {
            mLineColoringModel = null;
        } else if (coloringPoint.action == MotionEvent.ACTION_DOWN) {
            mLineColoringModel = new LineColoringModel();
        } else if (coloringPoint.action == MotionEvent.ACTION_MOVE) {
            mLineColoringModel.addPoint(coloringPoint);
        }
    }

    @Override
    public void addPoint(DrawingPoint addPoint) {
        if (mCurrentColoringPath == null) {
            mCurrentColoringPath = new ColoringPath(getContext());
            mCurrentColoringPath.getPaint().setColor(mCurrentColor);
            mColoringPaths.add(mCurrentColoringPath);
        }
        mCurrentColoringPath.addPoint(addPoint);
    }

    @Override
    public void finishCurrentPath() {
        mCurrentColoringPath = null;
    }

    public void removeTopGuidePath() {
        if (mColoringPaths.size() > 0) {
            mColoringPaths.remove(mColoringPaths.size() - 1);
        }
    }

    public void clear() {
        mColoringPaths.clear();
        invalidate();
    }
}
