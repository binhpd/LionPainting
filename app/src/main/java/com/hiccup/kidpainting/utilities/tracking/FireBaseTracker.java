package com.hiccup.kidpainting.utilities.tracking;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.hiccup.kidpainting.BuildConfig;

public class FireBaseTracker {

    private static FirebaseAnalytics mInstance;

    public static void init(Context context) {
        if (mInstance == null) {
            mInstance = FirebaseAnalytics.getInstance(context);
        }
    }

    public static void sendEvent(String eventName) {
        if (!BuildConfig.IS_DEBUG) {
            mInstance.logEvent(eventName, new Bundle());
        }
    }

}
