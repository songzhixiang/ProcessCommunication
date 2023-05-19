package com.andysong.server


import android.os.MemoryFile
import java.io.FileDescriptor
import java.io.IOException
/**
 * @name： ProcessCommunication
 * @author： SongZhiXiang
 * @time： 2023/5/19 13:27
 * @version： 1.0
 * @description：
 */
object MemoryFileUtils {

    fun createMemoryFile(name: String?, length: Int): MemoryFile? {
        try {
            return MemoryFile(name, length)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun getFileDescriptor(memoryFile: MemoryFile): FileDescriptor {
        return ReflectUtils.invoke(
            "android.os.MemoryFile",
            memoryFile,
            "getFileDescriptor"
        ) as FileDescriptor
    }
}