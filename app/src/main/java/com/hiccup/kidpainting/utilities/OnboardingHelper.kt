package com.hiccup.kidpainting.utilities

import android.app.Activity
import android.view.View
import com.hiccup.kidpainting.BuildConfig
import com.hiccup.kidpainting.R
import tourguide.tourguide.Overlay
import tourguide.tourguide.Pointer
import tourguide.tourguide.ToolTip
import tourguide.tourguide.TourGuide

internal object OnboardingHelper {
    private var mTourGuide: TourGuide? = null

    fun showBoarding(activity: Activity, title: String, gravityText: Int, gravityPointer: Int, view: View, prefKey: String) {
        if (StorageUtils.getBooleanFromSharedPref(activity, prefKey, false)) {
            return
        }
        val toolTip = ToolTip().setTitle(title).setGravity(gravityText)
        val pointer = Pointer()
        pointer.setGravity(gravityPointer)

        mTourGuide = TourGuide.init(activity).with(TourGuide.Technique.CLICK)
                .setPointer(pointer)
                .setToolTip(toolTip)
                .setOverlay(Overlay().setBackgroundColor(R.color.color_onboarding))
                .playOn(view)
        StorageUtils.writeBoolenToSharedPref(activity, prefKey, true)

        view.setOnTouchListener { v, event ->
            hideBoarding()
            false
        }
    }

    private fun hideBoarding() {
        if (mTourGuide != null) {
            mTourGuide!!
                    .cleanUp()
            mTourGuide = null
        }
    }
}
