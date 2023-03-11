package com.hiccup.kidpainting.drawinghelper.replay

import android.content.Context

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter

import com.hiccup.kidpainting.drawinghelper.BasePath
import com.hiccup.kidpainting.drawinghelper.paint.PaintFactory
import com.hiccup.kidpainting.drawinghelper.path.PathFactory
import com.hiccup.kidpainting.utilities.ExternalStorageHelper

/**
 * Created by ${binhpd} on 6/23/2017.
 * Cache file architecture
 * //
 * line
 * type path .. type paint .. color .. strokewidth .. coordinate...
 * ...
 * //
 */

object StateDrawingWriter {

    @Throws(IOException::class)
    fun saveState(context: Context, fileName: String, lineModels: List<BasePath>, width: Int, height: Int): File {
        val myFile = File(ExternalStorageHelper().getCacheStateStorageDir(context).toString() + "/" + fileName + ".txt")
        myFile.createNewFile()

        val fOut = FileOutputStream(myFile)
        val myOutWriter = OutputStreamWriter(fOut)
        val content = generateCacheContent(lineModels, width, height)
        myOutWriter.append(content)
        myOutWriter.close()
        fOut.close()
        return myFile
    }

    private fun generateCacheContent(lineModels: List<BasePath>, width: Int, height: Int): String {
        val content = StringBuilder()
        content.append(lineModels.size.toString() + "\n")
        for (lineModel in lineModels) {
            content.append(PathFactory.getPathType(lineModel).toString() + " " +
                    PaintFactory.getTypePaint(lineModel.paint).toString() + " "
                    + lineModel.paint.color + " "
                    + lineModel.paint.strokeWidth)

            val pointModels = lineModel.points
            for (pointModel in pointModels) {
                content.append(" " + pointModel.index.toString() + " " + pointModel.action.toString() + " " + pointModel.x / width + " " + pointModel.y / height)
            }
            content.append("\n")
        }
        return content.toString()
    }
}
