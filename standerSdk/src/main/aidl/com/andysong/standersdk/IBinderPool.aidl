// IBinderPool.aidl
package com.andysong.standersdk;

// Declare any non-default types here with import statements

interface IBinderPool {
   IBinder queryBinder(int binderCode);
}