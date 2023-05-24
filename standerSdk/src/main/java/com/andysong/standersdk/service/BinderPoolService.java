package com.andysong.standersdk.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.andysong.standersdk.common.BinderPool;

/**
 * @name： ProcessCommunication
 * @author： SongZhiXiang
 * @time： 2023/5/24 15:58
 * @version： 1.0
 * @description：
 */
public class BinderPoolService extends Service {

    private IBinder mBinder = new BinderPool.BinderPoolImpl();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
