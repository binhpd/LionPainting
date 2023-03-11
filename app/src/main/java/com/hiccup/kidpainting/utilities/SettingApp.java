package com.hiccup.kidpainting.utilities;

import android.content.Context;

/**
 * Created by ${binhpd} on 10/18/2015.
 */

public class SettingApp {
    public static boolean MUTE = false;
    public static final String SETTING_PREFERENCE_MUTE = "setting_preference_mute";
    public static final String SCHEDULE_DAILY_NOTIFICATION = "schedule_daily_notification";
    public static SettingApp mSettingApp;

    public static SettingApp getInstance() {
        if (mSettingApp == null) {
            return new SettingApp();
        } else {
            return mSettingApp;
        }
    }

    public boolean isScheduleDailyNotification(Context context) {
        return StorageUtils.getBooleanFromSharedPref(context, SCHEDULE_DAILY_NOTIFICATION, false);
    }

    public void markScheduleDailyNotification(Context context, boolean isSchedule) {
        StorageUtils.writeBoolenToSharedPref(context, SCHEDULE_DAILY_NOTIFICATION, isSchedule);
    }

}
