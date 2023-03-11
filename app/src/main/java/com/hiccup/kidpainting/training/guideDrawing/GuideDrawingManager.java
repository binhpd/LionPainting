package com.hiccup.kidpainting.training.guideDrawing;

import java.util.ArrayList;

import com.hiccup.kidpainting.models.LineColoringModel;
import com.hiccup.kidpainting.training.BaseManager;

/**
 * Created by ${binhpd} on 9/17/2016.
 */
public class GuideDrawingManager extends BaseManager {

    private ArrayList<LineColoringModel> lineColoringModels;

    public GuideDrawingManager(ArrayList<LineColoringModel> lineColoringModels) {
        this.lineColoringModels = lineColoringModels;
    }


    @Override
    public void start() {
        index++;

    }

    @Override
    public void finish() {

    }

    @Override
    public void next() {
        index++;
    }

    @Override
    public void restart() {
        index = -1;
    }
}
