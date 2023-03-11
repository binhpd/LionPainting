package com.hiccup.kidpainting.utilities;

import android.view.animation.Interpolator;

/**
 * Created by ${binhpd} on 10/22/2015.
 */
public class ReverseInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float paramFloat) {
        return Math.abs(paramFloat -1f);
    }
}