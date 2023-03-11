package com.hiccup.kidpainting.drawinghelper;

import java.io.Serializable;

/**
 * DrawingPoint object
 * Created by lion on 1/27/16.
 */
public class DrawingPoint implements Serializable {
    public int index;
    public int action;
    public float x;
    public float y;

    public DrawingPoint(int index, int action, float x, float y) {
        this.index = index;
        this.action = action;
        this.x = x;
        this.y = y;
    }

    public double distance(DrawingPoint p) {
        return sub(p).length();
    }

    public double distanceSqr(DrawingPoint p) {
        return sub(p).lengthSqr();
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public double lengthSqr() {
        return x * x + y * y;
    }

    public DrawingPoint sub(DrawingPoint p) {
        return new DrawingPoint(0, 0, x-p.x, y-p.y);
    }

    public DrawingPoint add(DrawingPoint p) {
        return new DrawingPoint(0, 0, x+p.x, y+p.y);
    }

    public void addBy(DrawingPoint p) {
        x += p.x;
        y += p.y;
    }

    public void mulBy(float s) {
        x *= s;
        y *= s;
    }

    public DrawingPoint normalized() {
        DrawingPoint n = new DrawingPoint(index, action, x, y);
        double length = length();
        n.x /= length;
        n.y /= length;
        return n;
    }

    public void normalize() {
        double length = length();
        x /= length;
        y /= length;
    }
}
