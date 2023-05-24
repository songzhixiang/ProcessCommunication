package com.andysong.standersdk.impl;

import android.os.RemoteException;

import com.andysong.standersdk.IAddOperation;

/**
 * @name： ProcessCommunication
 * @author： SongZhiXiang
 * @time： 2023/5/24 16:08
 * @version： 1.0
 * @description：
 */
public class AddOperationImpl extends IAddOperation.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
