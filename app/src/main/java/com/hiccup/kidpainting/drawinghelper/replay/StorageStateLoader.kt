package com.hiccup.kidpainting.drawinghelper.replay

import android.content.Context
import com.hiccup.kidpainting.utilities.ExternalStorageHelper
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class StorageStateLoader(context: Context, name: String, width: Int, height: Int) : BaseLoader(context, name, width, height) {
    override fun getBufferReader(): BufferedReader? {
        val myFile = File(ExternalStorageHelper().getCacheStateStorageDir(context).toString() + "/" + name + ".txt")
        var reader: BufferedReader? = null
        if (myFile.exists()) {
            reader = BufferedReader(FileReader(myFile))
        }
        return reader
    }

}
