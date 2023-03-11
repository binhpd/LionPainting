package com.hiccup.kidpainting.training.manager;

import android.view.animation.Interpolator;

/**
 * Created by binh.pd on 10/23/2015.
 */
public class ReverseInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float paramFloat) {
        return Math.abs(paramFloat -1f);
    }
}