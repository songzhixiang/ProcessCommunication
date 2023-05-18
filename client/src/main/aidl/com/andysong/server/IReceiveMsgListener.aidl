// IReceiveMsgListener.aidl
package com.andysong.server;

import com.andysong.server.Msg; // 这里需要导入对象

interface IReceiveMsgListener {
    void onReceive(in Msg msg);
}