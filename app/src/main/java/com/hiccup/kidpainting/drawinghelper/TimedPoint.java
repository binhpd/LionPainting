package com.hiccup.kidpainting.drawinghelper;

import java.io.Serializable;


/**
 * Created by lion on 6/29/16.
 */
public class TimedPoint implements Serializable {
    public final DrawingPoint point;
    public long time;

    public TimedPoint(DrawingPoint p, long dt) {
        this.point = p;
        this.time = dt;
    }
}
