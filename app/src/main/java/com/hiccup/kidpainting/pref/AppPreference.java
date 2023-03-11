package com.hiccup.kidpainting.pref;

import android.content.Context;
import com.hiccup.kidpainting.common.KidConfig;
import com.hiccup.kidpainting.utilities.StorageUtils;

public class AppPreference {
    public static final String SHOW_REVIEW = "pref_show_review";
    public static final String COUNT_SHARE_FACEBOOK_COLLECTION = "pref_count_share_facebook_collection";
    private static final AppPreference ourInstance = new AppPreference();

    public static AppPreference getInstance() {
        return ourInstance;
    }

    private AppPreference() {
    }

    public boolean isShowReview(Context context) {
        return StorageUtils.getBooleanFromSharedPref(context, SHOW_REVIEW, false);
    }

    public void setShowReview(Context  context, boolean showReview) {
        StorageUtils.writeBoolenToSharedPref(context, SHOW_REVIEW, showReview);
    }

    public boolean canShareFacebookForOpenCollection(Context context) {
        return KidConfig.MAX_COUNT_FACEBOOK_SHARE_COLLECTION > StorageUtils.getIntFromSharedPref(context, COUNT_SHARE_FACEBOOK_COLLECTION, 0);
    }

    public void increaseShareFacebookOpenCollection(Context context) {
        StorageUtils.writeIntToSharedPref(context,
                COUNT_SHARE_FACEBOOK_COLLECTION,
                StorageUtils.getIntFromSharedPref(context, COUNT_SHARE_FACEBOOK_COLLECTION, 0) + 1);
    }
}
