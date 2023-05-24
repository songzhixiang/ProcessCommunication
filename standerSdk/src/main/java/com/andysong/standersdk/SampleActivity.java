package com.andysong.standersdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.andysong.standersdk.common.BinderPool;
import com.andysong.standersdk.impl.AddOperationImpl;
import com.andysong.standersdk.impl.MinusOperationImpl;

public class SampleActivity extends AppCompatActivity {

    private IAddOperation mAddOperation;
    private IMinusOperation mMinusOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
    }

    private void testAdd(){
        BinderPool binderPool = BinderPool.getInstance(this);
        IBinder addBinder = binderPool.queryBinder(BinderPool.BINDER_ADD);
        mAddOperation = AddOperationImpl.asInterface(addBinder);
        try {
            mAddOperation.add(1,2);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void testMinus(){
        BinderPool binderPool = BinderPool.getInstance(this);
        IBinder minusBinder = binderPool.queryBinder(BinderPool.BINDER_MINUS);
        mMinusOperation = MinusOperationImpl.asInterface(minusBinder);
        try {
            mMinusOperation.minus(1,2);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}