package com.hiccup.kidpainting.core;

import android.os.Handler;
import android.os.SystemClock;

import com.hiccup.kidpainting.utilities.LogUtils;

/**
 * Created by ${binhpd} on 9/18/2016.
 */
public abstract class Updatable {
    private final long BEST_DELAY_MS = 16;
    private Handler handler = new Handler();
    private boolean stop = false;
    private boolean running = false;
    private long lastTickMs;
    private Runnable runnable;

    public abstract void update(long deltaTimeMs);

    public void start() {
        running = true;
        stop = false;
        lastTickMs = SystemClock.elapsedRealtime();
        runnable = new Runnable() {
            @Override
            public void run() {
                long dt = SystemClock.elapsedRealtime() - lastTickMs;
                lastTickMs += dt;
                update(dt);

                if (!stop) {
                    handler.postDelayed(runnable, BEST_DELAY_MS);
                }
            }
        };

        runnable.run();
    }

    public void stop() {
        stop = true;
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}
