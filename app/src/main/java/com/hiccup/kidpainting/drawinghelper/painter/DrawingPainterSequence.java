package com.hiccup.kidpainting.drawinghelper.painter;

import android.os.CountDownTimer;
import android.os.SystemClock;

import com.hiccup.kidpainting.drawinghelper.DrawingPoint;
import com.hiccup.kidpainting.drawinghelper.IDrawingView;
import com.hiccup.kidpainting.view.IColoringView;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by ${binhpd} on 3/26/2016.
 */
public class DrawingPainterSequence extends DrawingPainter {

    /**
     * Running time for CountDownTimer
     * Unit: Millisecond
     */
    private static final int COUNTDOWN_RUNNING_TIME = 10 * 60 * 1000;
    private static final int INTERVAL_TIME_MS = 300;
    private static final int TOTAL_FIX_TIME = 5000;
    private float mScreenRefreshRateMs; // == screen refresh time
    private int mIntervalDrawTimeMs;
    /*
     * Redundancy time when draw a point
     * Unit: Millisecond
     */
    private int mRedundancyTimeMS = 0;

    private long mLastTickMs;
    private long tickElapsedMs;

    public DrawingPainterSequence(IDrawingView view) {
        super(view);
    }

    @Override
    public void draw(@Nonnull List<DrawingPoint> points) {
        int oldSize = drawingPoints.size();
        drawingPoints.addAll(points);
        // Calculate intervalDrawTime
        int totalDrawTime = INTERVAL_TIME_MS + mIntervalDrawTimeMs * drawingPoints.size();
        if (totalDrawTime > TOTAL_FIX_TIME) {
            totalDrawTime = TOTAL_FIX_TIME;
        }
        mIntervalDrawTimeMs = totalDrawTime / drawingPoints.size();
    }

    /**
     * Initialize drawing task to draw point to view every @mScreenRefreshRateMs = device's screen refresh rate
     */
    public void initCountDownStatic() {
        final CountDownTimer countDownTimerStatic = new CountDownTimer(COUNTDOWN_RUNNING_TIME, (long) mScreenRefreshRateMs) {
            @Override
            public void onTick(long millisUntilFinished) {
                tickElapsedMs = SystemClock.elapsedRealtime() - mLastTickMs;
                mLastTickMs += tickElapsedMs;
                if (!drawingPoints.isEmpty()) {
                    mRedundancyTimeMS += tickElapsedMs;
                    if (mRedundancyTimeMS > mIntervalDrawTimeMs) {
                        int number = mRedundancyTimeMS / mIntervalDrawTimeMs;
                        for (int i = 0; i < number && !drawingPoints.isEmpty(); i++) {
                            mRedundancyTimeMS -= mIntervalDrawTimeMs;
                            drawPointToView(drawingPoints.get(0));
                            drawingPoints.remove(0);
                        }
                    }
                }
            }

            @Override
            public void onFinish() {
                initCountDownStatic();
            }
        };

        mLastTickMs = SystemClock.elapsedRealtime();
        countDownTimerStatic.start();
    }

    public void setScreenRefreshRateMs(float screenRefreshRateMs) {
        this.mScreenRefreshRateMs = screenRefreshRateMs;
    }
}
