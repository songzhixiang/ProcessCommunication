package com.andysong.server;

import com.andysong.server.Msg; // 这里需要导入对象
import com.andysong.server.IReceiveMsgListener; // 这里导入


interface IMsgManager {
    //发送信息
    void sendMsg(in Msg msg);
    //客户端注册信息
    void registerReceiveListener(IReceiveMsgListener listener);
    //移除注册
    void unregisterReceiveListener(IReceiveMsgListener listener);
}