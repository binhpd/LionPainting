package com.hiccup.kidpainting.utilities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.hiccup.kidpainting.activities.CollectionActivity


class RedirectHelper {
    companion object {
            private const val OPEN_COLLECTIONS = "open_collections"
            private const val OPEN_CH_PLAY = "open_ch_play"

        private const val EXTRA_ACTION = "extra_action"

        fun redirect(activity: Activity, intent: Intent) {
            intent?.extras?.let {
                if (it.containsKey(EXTRA_ACTION)) {
                    when (it.getString(EXTRA_ACTION)) {
                        OPEN_COLLECTIONS -> {
                            activity.startActivity(Intent(activity, CollectionActivity::class.java))
                        }
                        OPEN_CH_PLAY -> {
                            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.hiccup.kidpainting")))
                            activity?.finish()
                        }
                    }
                }
            }
        }
    }
}
