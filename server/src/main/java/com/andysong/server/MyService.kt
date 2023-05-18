package com.andysong.server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList
import android.os.RemoteException
import android.util.Log
import java.util.Date

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
       return MyBinder()
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

    }

    companion object{
        const val TAG = "MyService"
    }
}