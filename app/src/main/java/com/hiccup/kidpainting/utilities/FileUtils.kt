package com.hiccup.kidpainting.utilities

import android.content.Context
import android.content.res.AssetManager
import android.os.Environment
import android.util.Log

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FilenameFilter
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL

object FileUtils {
    @Throws(IOException::class)
    fun getDataContentByUrl(url: String): String {
        val data = URL(url)
        var input = BufferedReader(InputStreamReader(
                data.openStream()))

        var inputLine: String
        val builder = StringBuilder()

        while (true) {
            inputLine = input.readLine()
            if (inputLine == null) {
                break
            }
            builder.append(inputLine)
        }

        input.close()

        return builder.toString()
    }

    fun getDataContent(file: java.io.File): String {
        try {
            val fileIS = FileInputStream(file)
            val buf = BufferedReader(
                    InputStreamReader(fileIS))
            val data = StringBuilder()
            var readString: String
            // just reading each line and pass it on the debugger
            while (true) {
                readString = buf.readLine()
                if (readString == null) {
                    break
                }
                data.append(readString)
            }

            buf.close()
            return data.toString()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return ""
        } catch (e: IOException) {
            e.printStackTrace()
            return ""
        }

    }

    fun findFile(folder_path: String,
                 pattern: String): Array<java.io.File> {
        return java.io.File(folder_path).listFiles { dir, filename -> filename.matches(pattern.toRegex()) }
    }

    fun getStringFromAsset(ctx: Context, assetFile: String): String {
        val `is`: InputStream
        try {
            `is` = ctx.assets.open(assetFile)
            val size = `is`.available()

            // Read the entire asset into a local byte buffer.
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()

            // Convert the buffer into a Java string.
            return String(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return ""

    }
}
