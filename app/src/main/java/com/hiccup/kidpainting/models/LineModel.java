package com.hiccup.kidpainting.models;

import java.util.ArrayList;

import com.hiccup.kidpainting.drawinghelper.DrawingPoint;

/**
 * Created by hiccup on 23/08/2015.
 */
public class LineModel {
    private ArrayList<DrawingPoint> lineDraw;
    private float size;
    private int color;

    public LineModel() {
        lineDraw = new ArrayList<>();
    }

    public void add(DrawingPoint pointModel) {
        lineDraw.add(pointModel);
    }

    public ArrayList<DrawingPoint> getLine() {
        return lineDraw;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
