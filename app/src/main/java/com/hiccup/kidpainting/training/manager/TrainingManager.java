package com.hiccup.kidpainting.training.manager;


import java.util.ArrayList;

import com.hiccup.kidpainting.training.speakinglesson.TrainingDAO;
import com.hiccup.kidpainting.training.speakinglesson.TrainingModel;

/**
 * Created by binh.pd on 10/23/2015.
 */
public abstract class TrainingManager implements TrainingDAO {
    protected int currentPlay;
    protected int lastPlay;
    protected int nextPlay;

    public TrainingManager() {
        currentPlay = 0;
        lastPlay = nextPlay = -1;
    }
    public ArrayList<TrainingModel> listLessons;

    @Override
    public void setListLesson(ArrayList<TrainingModel> listLessons) {
        this.listLessons = listLessons;
    }

}
