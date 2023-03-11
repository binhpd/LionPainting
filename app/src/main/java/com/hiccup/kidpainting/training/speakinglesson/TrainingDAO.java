package com.hiccup.kidpainting.training.speakinglesson;

import java.util.ArrayList;

/**
 * Created by binh.pd on 10/23/2015.
 */
public interface TrainingDAO {
    void startLesson();
    void setListLesson(ArrayList<TrainingModel> listLesson);
    void nextLesson();
    void forwardLesson();
}
