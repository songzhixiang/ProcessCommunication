package com.andysong.server

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * @name： ProcessCommunication
 * @author： SongZhiXiang
 * @time： 2023/5/18 15:01
 * @version： 1.0
 * @description：
 */
class MyService: Service() {

    private val listeners = RemoteCallbackList<IReceiveMsgListener>()

    override fun onBind(intent: Intent?): IBinder {
       return when(intent?.getIntExtra("bigDataVersion",-1)){
           0 -> MyBinder()
           1 -> MyBigDataBinder()
           else -> MyBinder()
       }
    }

    inner class MyBinder : IMsgManager.Stub(){
        override fun sendMsg(msg: Msg?) {
            Log.e(TAG, "client sendMsg to Server : ${msg?.msg}")
            val n = listeners.beginBroadcast()
            for (i in 0 until n){
                val broadcastItem = listeners.getBroadcastItem(i)
                try {
                    val serverMsg = Msg("服务器响应 ${Date(System.currentTimeMillis())}")
                    broadcastItem.onReceive(serverMsg)
                }catch (e : RemoteException){
                    e.printStackTrace()
                }
            }
            listeners.finishBroadcast()
        }

        override fun registerReceiveListener(listener: IReceiveMsgListener?) {
            Log.e(TAG, "registerReceiveListener: 注册成功")
            listeners.register(listener)
        }

        override fun unregisterReceiveListener(listener: IReceiveMsgListener?) {
            val success = listeners.unregister(listener)
            if (success){
                Log.e(TAG, "unregisterReceiveListener: 解除成功")
            }else {
                Log.e(TAG, "unregisterReceiveListener: 解除失败")
            }
        }

        //package鉴权
        override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
            val check = checkCallingPermission("app.service.permission")
            Log.e(TAG, "onTransact: check-$check,thread-${Thread.currentThread()}")
            if (check == PackageManager.PERMISSION_DENIED){
                return false;
            }
            val packages = packageManager.getPackagesForUid(getCallingUid())
            //判断一下调用的包名
            if (false == packages?.first()?.startsWith("com.andysong")){
                return false
            }
            return super.onTransact(code, data, reply, flags)
        }
    }

    inner class MyBigDataBinder : IOptionsBigData.Stub(){
        override fun transactFileDescriptor(pfd: ParcelFileDescriptor?) {
           val file = File(cacheDir,"file.mp4")
            try {
                val inputStream = ParcelFileDescriptor.AutoCloseInputStream(pfd)
                file.delete()
                file.createNewFile()
                val stream = FileOutputStream(file)
                val buffer = ByteArray(1024)
                var len: Int
                // 将inputStream中的数据写入到file中
                while (inputStream.read(buffer).also { len = it } != -1) {
                    stream.write(buffer, 0, len)
                }
                stream.close()
                pfd?.close()
            }catch (e:IOException){
                e.printStackTrace()
            }
        }

    }

    companion object{
        const val TAG = "MyService"
    }
}