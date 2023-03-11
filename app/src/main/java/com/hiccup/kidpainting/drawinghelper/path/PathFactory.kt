package com.hiccup.kidpainting.drawinghelper.path

import android.content.Context
import com.hiccup.kidpainting.drawinghelper.BasePath

/**
 * Created by ${binhpd} on 3/27/2016.
 */
object PathFactory {
    const val NORMAL_PATH = 0

    fun getPath(context: Context, pathType: Int): BasePath {
        when (pathType) {
            NORMAL_PATH -> return NormalPath(context)
            else -> {
            }
        }
        return FadeOutPath(context)
    }

    fun getPathType(path: BasePath): Int {
        if (path is NormalPath) {
            return NORMAL_PATH
        }
        return NORMAL_PATH
    }
}
