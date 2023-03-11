package com.hiccup.kidpainting.drawinghelper.replay;

import com.hiccup.kidpainting.core.Updatable;
import com.hiccup.kidpainting.drawinghelper.BasePath;
import com.hiccup.kidpainting.drawinghelper.DrawingPoint;
import com.hiccup.kidpainting.models.PathData;
import com.hiccup.kidpainting.view.IColoringView;

/**
 * Created by lion on 6/15/16.
 */
public abstract class ReplayDrawingSequence {
    public static final int MODE_PREVIEW = 1;
    public static final int MODE_MANUAL = 0;
    private static final int TIME_OUT_ADD_STICKER = 3000;

    private Updatable updatable;

    private final IColoringView drawingViewBase;

    private PathData mPathData;

    private BasePath mCurrentLine;

    private int mMode = MODE_MANUAL;

    private int indexPoint = 0;
    private boolean isFinished = false;

    public ReplayDrawingSequence(IColoringView drawingViewBase) {
        this.drawingViewBase = drawingViewBase;
    }

    public void setPath(PathData path) {
        mPathData = path;
    }

    public void setMode(int mode) {
        this.mMode = mode;
    }

    /**
     * start drawing sticker
     *
     * @return position draw sticker
     */
    public void startDispatchPoint() {
        if (updatable != null && updatable.isRunning()) {
            return;
        }

        updatable = new Updatable() {
            @Override
            public void update(long deltaTimeMs) {
                updateMySticker(deltaTimeMs);
            }
        };
        updatable.start();
    }

    public void next() {
        if (isRunning()) {
            return;
        }
        if (mPathData.hasNext()) {
            mCurrentLine = mPathData.nextLine();
            drawingViewBase.createNewPaint(mCurrentLine.getPaint());
            startDispatchPoint();
        } else {
            mCurrentLine = null;
        }
    }

    public void previous() {
        if (isRunning()) {
            return;
        }

        if (mCurrentLine != null) {
            resetPointIndex();
            mCurrentLine = null;
        }

        drawingViewBase.removeTopGuidePath();
        drawingViewBase.removeTopGuidePath();

        mCurrentLine = mPathData.previousLine();
        if (mCurrentLine != null) {
            drawingViewBase.createNewPaint(mCurrentLine.getPaint());
            resetPointIndex();
        }
    }

    public void replay() {
        if (isRunning()) {
            return;
        }
        drawingViewBase.removeTopGuidePath();
        if (mCurrentLine != null) {
            resetPointIndex();

        }
    }

    private void resetPointIndex() {
        indexPoint = 0;
    }

    /**
     * dispatcher stickers point to corresponding path
     *
     * @param dt
     */
    private void updateMySticker(long dt) {
        if (mCurrentLine == null || indexPoint > mCurrentLine.getPoints().size() - 1) {
            resetPointIndex();
            return;
        }

        int size = mCurrentLine.getPoints().size();
        int countPointsAdded = (size * (int) dt) / TIME_OUT_ADD_STICKER;
        if (countPointsAdded == 0) {
            countPointsAdded = 2;
        }

        for (int i = 0; i < countPointsAdded; i++) {
            DrawingPoint drawingPoint = getNextPoint();
            if (indexPoint == 0) {
                drawingViewBase.createNewPaint(mCurrentLine.getPaint());
            }
            drawingViewBase.onDrawGuidePath(drawingPoint);
            if (!hasNextPoint()) {
                onFinishDispatchLine(mPathData.isLastPath());
                isFinished = true;
                break;
            }
            indexPoint++;
        }
    }

    private DrawingPoint getNextPoint() {
        DrawingPoint point = mCurrentLine.getPoints().get(indexPoint);
        return point;
    }

    private boolean hasNextPoint() {
        return indexPoint < mCurrentLine.getPoints().size() - 1;
    }

    /**
     * handle event finished read line
     *
     * @param isLastPath
     */
    private void onFinishDispatchLine(boolean isLastPath) {
        resetPointIndex();
        if (mMode == MODE_MANUAL) {
            onFinishReadLine();
            updatable.stop();
            drawingViewBase.finishCurrentPath();
        } else if (mMode == MODE_PREVIEW) {
            if (isLastPath) {
                onFinishReadLine();
            }
            updatable.stop();
            drawingViewBase.finishCurrentPath();
            next();
        }
    }

    protected abstract void onFinishReadLine();

    protected abstract void onAddPoint(int action, float x, float y);

    public boolean isRunning() {
        return updatable != null && updatable.isRunning();
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void stop() {
        if (updatable != null) {
            updatable.stop();
        }
    }

    public void resume() {
        if (updatable != null) {
            updatable.start();
        }
    }
}
