package com.hiccup.kidpainting.drawinghelper.replay

import android.content.Context
import android.os.AsyncTask
import com.hiccup.kidpainting.drawinghelper.BasePath
import com.hiccup.kidpainting.drawinghelper.DrawingPoint
import com.hiccup.kidpainting.drawinghelper.paint.PaintFactory
import com.hiccup.kidpainting.drawinghelper.path.PathFactory
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.lang.Float

abstract class BaseLoader(val context: Context, val name: String, val width: Int, val height: Int) : AsyncTask<Void, Void, List<BasePath>>() {
    var listener: LoadListener? = null

    override fun doInBackground(vararg params: Void?): List<BasePath>? {
        var reader: BufferedReader? = getBufferReader()
        return if (reader != null) {
            readState(context, reader, width, height)
        } else {
            null
        }
    }

    abstract fun getBufferReader(): BufferedReader?

    private fun readState(context: Context, reader: BufferedReader, width: Int, height: Int): List<BasePath> {
        val paths = ArrayList<BasePath>()
        try {
            reader.readLine()
            while (true) {
                val text = reader.readLine() ?: break

                val data = text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                val normalPath = PathFactory.getPath(context, Integer.parseInt(data[0]))
                val paint = PaintFactory.getPaint(context, Integer.parseInt(data[1]))
                paint.color = Integer.parseInt(data[2])
                normalPath.paint = paint

                var i = 4
                val size = data.size
                while (i < size) {
                    val coloringPoint = DrawingPoint(data[i].toInt(), data[i+1].toInt(),
                            data[i+2].toFloat() * width,
                            data[i + 3].toFloat() * height)

                    normalPath.addPoint(coloringPoint)
                    i += 4
                }

                paths.add(normalPath)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return paths
    }

    override fun onPostExecute(result: List<BasePath>?) {
        super.onPostExecute(result)
        listener?.onLoadSuccess(result)

    }

    interface LoadListener {
        fun onLoadSuccess(result: List<BasePath>?)
    }
}
