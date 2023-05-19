package com.andysong.client

import android.content.Context
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
/**
 * @name： ProcessCommunication
 * @author： SongZhiXiang
 * @time： 2023/5/19 14:10
 * @version： 1.0
 * @description：
 */
object AssetUtils {

    fun openAssets(context: Context, fileName: String): ByteArray? {
        try {
            val inputStream = context.assets.open(fileName)
            return openInputStream(context, inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun openInputStream(context: Context?, inputStream: InputStream): ByteArray? {
        try {
            val baos = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len = -1
            while (inputStream.read(buffer).also { len = it } >= 0) {
                baos.write(buffer, 0, len)
            }
            baos.flush()
            baos.close()
            inputStream.close()
            return baos.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}