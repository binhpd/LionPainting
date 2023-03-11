package com.hiccup.kidpainting.models;

import com.hiccup.kidpainting.drawinghelper.BasePath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${binhpd} on 6/25/2017.
 * Path data include all lines
 */

public class PathData {

    private List<BasePath> lineColoringModels;

    private int index = -1;

    public void setLineColoringModels(List<BasePath> lineColoringModels) {
        this.lineColoringModels = lineColoringModels;
    }

    public boolean hasNext() {
        if (lineColoringModels == null) {
            return false;
        }
        return index < lineColoringModels.size() - 1;
    }

    public BasePath nextLine() {
        index++;
        return lineColoringModels.get(index);
    }

    public BasePath previousLine() {
        BasePath lineColoringModel;
        index--;
        if (index < 0) {
            return null;
        }
        lineColoringModel = lineColoringModels.get(index);
        return lineColoringModel;
    }

    public boolean isLastPath() {
        return index == lineColoringModels.size() - 1;
    }
}
