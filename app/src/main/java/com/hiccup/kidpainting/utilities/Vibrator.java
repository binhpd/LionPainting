package com.hiccup.kidpainting.utilities;

import android.content.Context;

import com.hiccup.kidpainting.annotation.ContextualInitializer;

/**
 * Created by ${binhpd} on 3/27/2016.
 */
@ContextualInitializer
public class Vibrator {
    public static final long LONG = 100;
    public static final long SHORT = 50;

    private static android.os.Vibrator vibrator;

    public static void initialize(Context context) {
        Assert.assertTrue(vibrator == null, "Don't call me twice");
        vibrator = (android.os.Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public static void vibrate(long milisecs) {
        vibrator.vibrate(milisecs);
    }
}
