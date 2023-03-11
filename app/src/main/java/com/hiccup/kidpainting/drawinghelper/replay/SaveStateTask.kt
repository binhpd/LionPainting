package com.hiccup.kidpainting.drawinghelper.replay

import android.content.Context
import android.os.AsyncTask
import com.hiccup.kidpainting.drawinghelper.BasePath

class SaveStateTask(val context: Context, private val fileName: String,
                    private val lineModels: List<BasePath>, val width: Int, val height: Int) : AsyncTask<Void, Void, Void>() {

    override fun doInBackground(vararg params: Void?): Void?{
        StateDrawingWriter.saveState(context, fileName, lineModels, width, height)
        return null
    }
}
