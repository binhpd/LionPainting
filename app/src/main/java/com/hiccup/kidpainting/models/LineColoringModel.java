package com.hiccup.kidpainting.models;

import java.util.ArrayList;

import com.hiccup.kidpainting.drawinghelper.DrawingPoint;

/**
 * Created by ${binhpd} on 3/30/2016.
 */
public class LineColoringModel {
    private ArrayList<DrawingPoint> drawingPoints;
    private int color;
    private int index = 0;


    public LineColoringModel() {
        drawingPoints = new ArrayList<>();
    }

    public LineColoringModel(ArrayList<DrawingPoint> drawingPoints, int color) {
        this.drawingPoints = drawingPoints;
        this.color = color;
    }

    public ArrayList<DrawingPoint> getDrawingPoints() {
        return drawingPoints;
    }

    public void setDrawingPoints(ArrayList<DrawingPoint> drawingPoints) {
        this.drawingPoints = drawingPoints;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void addPoint(DrawingPoint coloringPoint) {
        drawingPoints.add(coloringPoint);
    }

    public void clearPoint() {
        drawingPoints.clear();
    }

    public DrawingPoint getNext() {
        DrawingPoint drawingPoint = drawingPoints.get(index);
        index ++;
        return drawingPoint;
    }

    public boolean hasNext() {
        return index < drawingPoints.size()-1;
    }

    public int getSize() {
        return drawingPoints.size();
    }

    public void reset() {
        index = 0;
    }
}
