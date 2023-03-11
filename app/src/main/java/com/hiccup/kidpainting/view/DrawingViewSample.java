package com.hiccup.kidpainting.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.hiccup.kidpainting.R;
import com.hiccup.kidpainting.common.PaintingType;
import com.hiccup.kidpainting.drawinghelper.DrawingPoint;
import com.hiccup.kidpainting.models.LineModel;
import com.hiccup.kidpainting.utilities.StackHelper;

import java.util.ArrayList;

/**
 * Created by hiccup on 21/08/2015.
 */
public class DrawingViewSample extends View {

    public static final int NORMAL_MODE = 0;
    public static final int PREVIEW_MODE = 1;

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    protected Paint mDrawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;

    private float brushSize, lastBrushSize;

    private boolean erase = false;

    private ArrayList<LineModel> linesList;
    private LineModel mLineModel;
    private int mType;

    protected int mWidth, height;

    private DrawingPoint mLastPoint;

    /**
     * drawing mode
     */
    private int mMode = NORMAL_MODE;

    public DrawingViewSample(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
        linesList = new ArrayList<>();
        mType = PaintingType.BRUSH;
    }

    private void setupDrawing() {
        //get drawing area setup for interaction
        drawPath = new Path();
        mDrawPaint = new Paint();
        mDrawPaint.setColor(paintColor);
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setStrokeWidth(20);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);

        brushSize = getResources().getDimensionPixelSize(R.dimen.brush_size_2);
        lastBrushSize = brushSize;
        mDrawPaint.setStrokeWidth(brushSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        height = h;
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
        // TODO: fix undo
//        for (int i = 0; i < )
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw view=
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, mDrawPaint);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mMode == PREVIEW_MODE) {
            return true;
        }
        //detect user touch
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                mLineModel = new LineModel();
                mLastPoint = null;
                mLineModel.setSize(brushSize);
                mLineModel.setColor(paintColor);
                mLineModel.add(new DrawingPoint(0, event.getAction(), touchX, touchY));
                linesList.add(mLineModel);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mType == PaintingType.BRUSH || mType == PaintingType.ERASER) {
                    mLineModel.add(new DrawingPoint(0, event.getAction(), touchX, touchY));
                    if (mLastPoint == null) {
                        drawPath.lineTo(touchX, touchY);
                    } else {
                        drawPath.quadTo(mLastPoint.x, mLastPoint.y, (mLastPoint.x + touchX) / 2, (mLastPoint.y + touchY) / 2);
                    }
                    mLastPoint = new DrawingPoint(0, event.getAction(), touchX, touchY);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mType == PaintingType.BRUSH || mType == PaintingType.ERASER) {
                    drawCanvas.drawPath(drawPath, mDrawPaint);
                    drawPath.reset();
                }
                break;
            default:
                return false;
        }
        return true;
    }

    public void setColor(String newColor) {
        //set color
        invalidate();
        paintColor = Color.parseColor(newColor);
        mDrawPaint.setColor(paintColor);
    }

    public void setColor(int color) {
        invalidate();
        paintColor = color;
        mDrawPaint.setColor(paintColor);
    }

    public void setBrushSize(float newSize) {
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize = pixelAmount;
        mDrawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize) {
        lastBrushSize = lastSize;
    }

    public float getLastBrushSize() {
        return lastBrushSize;
    }

    public void setErase(boolean isErase) {
        //set erase true or false
        erase = isErase;
        if (erase) {
            mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            mDrawPaint.setXfermode(null);
        }
    }

    public void startNew() {
        linesList = new ArrayList<>();
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void undo() {
        if (linesList.size() > 0) {
            setErase(false);
            drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            invalidate();
            linesList.remove(linesList.size() - 1);
            for (LineModel lineModel : linesList) {
                drawLine(lineModel);
                invalidate();
            }
            brushSize = lastBrushSize;
            mDrawPaint.setColor(paintColor);
            mDrawPaint.setStrokeWidth(lastBrushSize);
        }

    }

    private void drawLine(LineModel lineModel) {
        ArrayList<DrawingPoint> pointModels = lineModel.getLine();
        int size = pointModels.size();
        if (size > 1) {
            mDrawPaint.setColor(lineModel.getColor());
            mDrawPaint.setAntiAlias(true);
            mDrawPaint.setStrokeWidth(20);
            mDrawPaint.setStyle(Paint.Style.STROKE);
            mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
            mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
            mDrawPaint.setStrokeWidth(lineModel.getSize());
            drawPath.moveTo(pointModels.get(0).x, pointModels.get(0).y);
            DrawingPoint p0, p1;
            p0 = pointModels.get(0);
            drawPath.moveTo(p0.x, p0.y);

            for (int i = 1; i < size; i++) {
                p1 = pointModels.get(i);
                drawPath.quadTo(p0.x, p0.y, (p0.x + p1.x) / 2, (p0.y + p1.y) / 2);
                p0 = p1;
            }

            drawCanvas.drawPath(drawPath, mDrawPaint);
            drawPath.reset();
        }
    }

    //----------------------------------------------------------------------------------------------
    public void setType(int mType) {
        this.mType = mType;
    }

    public int getType() {
        return mType;
    }

    public void setMode(int mode) {
        mMode = mode;
    }

}
