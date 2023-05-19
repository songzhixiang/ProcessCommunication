package com.andysong.server;

import com.andysong.server.IMemoryFileListener;

interface IMemoryFileInterface{
    void sendImage(in byte[]data);
    void client2server(in ParcelFileDescriptor pdf);
    void registerCallback(IMemoryFileListener listener);
    void unregisterCallback(IMemoryFileListener listener);
}