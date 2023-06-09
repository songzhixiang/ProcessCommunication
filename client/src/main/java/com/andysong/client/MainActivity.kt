package com.andysong.client

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.MemoryFile
import android.os.ParcelFileDescriptor
import android.os.RemoteException
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.andysong.server.IMemoryFileInterface
import com.andysong.server.IMsgManager
import com.andysong.server.IOptionsBigData
import com.andysong.server.IReceiveMsgListener
import com.andysong.server.Msg
import java.io.File
import java.io.IOException

const val TAG = "Client"

class MainActivity : AppCompatActivity() {

    private lateinit var openButton: Button
    private lateinit var sendButton: Button
    private lateinit var sendBigDataButton: Button
    private var iOptionsBigData:IOptionsBigData? = null
    private var iMsgManager:IMsgManager? = null

    private var iMemoryFileInterface: IMemoryFileInterface? = null
    private var isBind = false

    private val connection = object :ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e(TAG, "onServiceConnected: OK")
            isBind = true
//            iMsgManager = IMsgManager.Stub.asInterface(service)
//            try {
//                iMsgManager?.asBinder()?.linkToDeath(deathRecipient,0)
//                iMsgManager?.registerReceiveListener(receiverMsgListener)
//            }catch (e:RemoteException){
//                e.printStackTrace()
//            }
            iOptionsBigData = IOptionsBigData.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e(TAG, "onServiceDisconnected: OK")
            isBind = false

        }

        override fun onBindingDied(name: ComponentName?) {
            Log.e(TAG, "onBindingDied: $name")
            super.onBindingDied(name)
        }

        override fun onNullBinding(name: ComponentName?) {
            Log.e(TAG, "onNullBinding: $name")
            super.onNullBinding(name)
        }
    }

    override fun onDestroy() {
        if (iMsgManager?.asBinder()?.isBinderAlive == true){
            try {
                iMsgManager?.unregisterReceiveListener(receiverMsgListener)
            }catch (e:RemoteException){
                e.printStackTrace()
            }
        }
        unbindService(connection)
        super.onDestroy()
    }

    //：当 Binder 死亡的时候，系统会回调此方法,当binder断开连接的时候解除注册
    private val deathRecipient = object :IBinder.DeathRecipient{
        override fun binderDied() {
            iMsgManager?.asBinder()?.unlinkToDeath(this,0)
            iMsgManager = null
        }

    }

    //这里要继承Stub
    private val receiverMsgListener = object :IReceiveMsgListener.Stub(){
        override fun onReceive(msg: Msg?) {
            Log.e(TAG, "onReceive: 收到信息 ${msg?.msg}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openButton = findViewById(R.id.btn_open)
        sendButton = findViewById(R.id.btn_send)
        sendBigDataButton = findViewById(R.id.btn_bigdata)
        sendButton.setOnClickListener {
            iMsgManager?.sendMsg(Msg("Hello Im Client",123L))
        }
        openButton.setOnClickListener {
            startOtherProcessService(false)
        }
        sendBigDataButton.setOnClickListener {
           transferData()
        }
    }

    private fun startOtherProcessService(boolean: Boolean){
        val intent= Intent()
        intent.setAction("android.intent.action.MyService")
        intent.setPackage("com.andysong.server")
        intent.putExtra("bigDataVersion",if (boolean) 0 else 1)
        bindService(intent,connection, BIND_AUTO_CREATE)
    }

    private fun transferData(){
        val fileDescriptor = ParcelFileDescriptor.open(
            File(cacheDir, "file.mp4"),
            ParcelFileDescriptor.MODE_READ_ONLY
        )
        iOptionsBigData?.transactFileDescriptor(fileDescriptor)
        fileDescriptor.close()
        // 调用AIDL接口，将文件描述符的读端 传递给 接收方
    }


    /**
     * 发送小型数据
     */
    private fun sendSmallData(){
        if (iMemoryFileInterface == null){
            return
        }
        val bytes = AssetUtils.openAssets(this, "small.jpg")
        try {
            iMemoryFileInterface?.sendImage(bytes)
        } catch (e: IOException) {
            e.printStackTrace()
        }catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    /**
     * 这里使用共享内存方式，但是文件占内存的大小，那么共享内存就会占多大
     */
    private fun sendBigData(){
        val inputStream = assets.open("large.jpg")
        val bytes = inputStream.readBytes()
        //创建MemoryFile
        val memoryFile = MemoryFile("client_image", bytes.size)
        // 向MemoryFile中写入字节数组
        memoryFile.writeBytes(bytes,0,0,bytes.size)
        //获取MemoryFile对应的FileDescriptor
        val fileDescriptor = MemoryFileUtils.getFileDescriptor(memoryFile)
        //根据FileDescriptor创建ParcelFileDescriptor
        val descriptor = ParcelFileDescriptor.dup(fileDescriptor)
        //发送数据
        iMemoryFileInterface?.client2server(descriptor)
    }
}