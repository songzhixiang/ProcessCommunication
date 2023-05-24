package com.andysong.standersdk.impl;

import android.os.RemoteException;

import com.andysong.standersdk.IMinusOperation;

/**
 * @name： ProcessCommunication
 * @author： SongZhiXiang
 * @time： 2023/5/24 16:08
 * @version： 1.0
 * @description：
 */
public class MinusOperationImpl extends IMinusOperation.Stub {
    @Override
    public int minus(int a, int b) throws RemoteException {
        return a * b;
    }
}
