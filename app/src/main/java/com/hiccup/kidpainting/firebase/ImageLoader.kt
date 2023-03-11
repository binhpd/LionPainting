package com.hiccup.kidpainting.firebase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import java.io.File


class ImageLoader(private val filePath: String, val listener: LoadImageListener) : AsyncTask<Void, Void, Bitmap>() {

    override fun doInBackground(vararg params: Void?): Bitmap? {
        val file = File(filePath)
        return if (file.exists()) {
            BitmapFactory.decodeStream(file.inputStream())
        } else {
            return null
        }
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        if (result != null) {
            listener.onLoadSuccess(result)
        } else {
            listener.loadFail()
        }
    }

    interface LoadImageListener {
        fun onLoadSuccess(bitmap: Bitmap)

        fun loadFail()
    }
}
