package com.andysong.server;

interface IMemoryFileListener{
    void server2client(in ParcelFileDescriptor pfd);
}