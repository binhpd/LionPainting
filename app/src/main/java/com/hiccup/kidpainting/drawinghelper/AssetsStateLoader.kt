package com.hiccup.kidpainting.drawinghelper

import android.content.Context

import com.hiccup.kidpainting.drawinghelper.replay.BaseLoader
import com.hiccup.kidpainting.models.PathData

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.ArrayList

/**
 * Created by ${binhpd} on 4/25/2017.
 */

class AssetsStateLoader(context: Context, name: String, width: Int, height: Int) : BaseLoader(context, name, width, height) {
    override fun getBufferReader(): BufferedReader? {
        return BufferedReader(InputStreamReader(context.assets.open(name)))
    }
}
