package com.hiccup.kidpainting.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class RemoteConfigHelper {
    public static final int FEATURE_DISABLE = 0;
    public static final int FEATURE_NEED_UPDATE = 1;
    public static final int FEATURE_AVAILABLE = 2;

    private static final RemoteConfigHelper ourInstance = new RemoteConfigHelper();
    private static final int PUBLIC_FEATURE_VERSION = 15;

    private FirebaseRemoteConfig firebaseRemoteConfig;

    private RemoteConfigHelper() {
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    }

    public static RemoteConfigHelper getInstance() {
        return ourInstance;
    }

    public interface RemoteKey {
        String LEARN_DRAWING_VERSION_CODE = "learn_drawing_version_code";
    }

    public int getStateNewFeature(Context context) {
        if (getLearningVersionCode() < PUBLIC_FEATURE_VERSION) {
            return FEATURE_DISABLE;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (packageInfo.versionCode >= PUBLIC_FEATURE_VERSION) {
                return FEATURE_AVAILABLE;
            } else {
                return FEATURE_NEED_UPDATE;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return FEATURE_DISABLE;
    }

    public int getLearningVersionCode() {
        return (int) firebaseRemoteConfig.getLong(RemoteKey.LEARN_DRAWING_VERSION_CODE);
    }
}
