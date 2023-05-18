package com.andysong.server

import android.os.Parcel
import android.os.Parcelable

/**
 * @name： ProcessCommunication
 * @author： SongZhiXiang
 * @time： 2023/5/18 15:03
 * @version： 1.0
 * @description：
 */
class Msg(var msg:String,var time:Long? = null):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readValue(Long::class.java.classLoader) as? Long
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(msg)
        parcel.writeValue(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Msg> {
        override fun createFromParcel(parcel: Parcel): Msg {
            return Msg(parcel)
        }

        override fun newArray(size: Int): Array<Msg?> {
            return arrayOfNulls(size)
        }
    }
}