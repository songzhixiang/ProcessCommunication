package com.andysong.client

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.andysong.server.IMsgManager
import com.andysong.server.IReceiveMsgListener
import com.andysong.server.Msg

const val TAG = "Client"

class MainActivity : AppCompatActivity() {

    private lateinit var openButton: Button
    private lateinit var sendButton: Button

    private var iMsgManager:IMsgManager? = null
    private var isBind = false

    private val connection = object :ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e(TAG, "onServiceConnected: OK")
            isBind = true
            iMsgManager = IMsgManager.Stub.asInterface(service)
            try {
                iMsgManager?.asBinder()?.linkToDeath(deathRecipient,0)
                iMsgManager?.registerReceiveListener(receiverMsgListener)
            }catch (e:RemoteException){
                e.printStackTrace()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBind = false
            Log.e(TAG, "onServiceDisconnected: OK")
        }

        override fun onBindingDied(name: ComponentName?) {
            super.onBindingDied(name)
        }

        override fun onNullBinding(name: ComponentName?) {
            super.onNullBinding(name)
        }
    }

    private val deathRecipient = IBinder.DeathRecipient {

    }

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
        sendButton.setOnClickListener {
            iMsgManager?.sendMsg(Msg("Hello Im Client",123L))
        }
        openButton.setOnClickListener {
            startOtherProcessService()
        }
    }

//    public fun startSameProcessService(){
//        val intent = Intent(this,MyService.class.java)
//        bindService(intent,connection, BIND_AUTO_CREATE)
//    }

    private fun startOtherProcessService(){
        val intent= Intent()
        intent.setAction("android.intent.action.MyService")
        intent.setPackage("com.andysong.server")
        bindService(intent,connection, BIND_AUTO_CREATE)
    }
}