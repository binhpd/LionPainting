package com.hiccup.kidpainting.utilities

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream


class ExternalStorageHelper {
    /* Checks if external storage is available for read and write */
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /* Checks if external storage is available to at least read */
    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

    fun getCacheStateStorageDir(context: Context): File? {
        // Get the directory for the app's private pictures directory.
        val file = File(context.getExternalFilesDir(""), "cache")
        if (!file?.mkdirs()!!) {
            Log.e("error", "Directory not created")
        }
        return file
    }

    fun getPublicAlbumStorageDir(context: Context, albumName: String): File? {
        // Get the directory for the user's public pictures directory.
        val file = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES + "/KidPainting"), albumName)
        if (!file?.mkdirs()!!) {
            Log.e("ExternalStorageHelper", "Directory not created")
        }
        return file
    }

    private fun getPrivateAlbumStorageDir(context: Context, albumName: String): File? {
        // Get the directory for the app's private pictures directory.
        val file = File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName)
        if (!file?.mkdirs()!!) {
            Log.e("ExternalStorageHelper", "Directory not created")
        }
        return file
    }

    /**
     * save bitmap to external storage if can
     * return path of file if saving is successfully otherwise return null
     */
    fun saveFileToExternalStorage(context: Context, fileName: String, bitmap: Bitmap): String? {
        val file = getPublicAlbumStorageDir(context, fileName) ?: return null

        if (file.exists())
            file.delete()
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
            return file.toString()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null

    }
}