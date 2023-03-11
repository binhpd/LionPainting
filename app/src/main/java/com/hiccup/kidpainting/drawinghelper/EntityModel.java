package com.hiccup.kidpainting.drawinghelper;

import java.util.ArrayList;

/**
 * Created by lion on 3/23/17.
 */

public class EntityModel {
    private ArrayList<TimedPoint> timedPoints;
    private int index = 0;

    public EntityModel(ArrayList<TimedPoint> timedPoints) {
        this.timedPoints = timedPoints;
    }

    public ArrayList<TimedPoint> getTimedPoints() {
        return timedPoints;
    }

    public TimedPoint getPoint() {
        return timedPoints.get(index);
    }

    public void setTimedPoints(ArrayList<TimedPoint> timedPoints) {
        this.timedPoints = timedPoints;
    }

    public boolean hasNext() {
        return index+1 <= timedPoints.size()-1;
    }

    public int getSizePoints() {
        return timedPoints.size();
    }

    public void moveToNext() {
        index++;
    }


}
