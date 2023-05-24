package com.example.shorts_task.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class APIdata (
    val `data` : Data,
    val message : String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("data"),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<APIdata> {
        override fun createFromParcel(parcel: Parcel): APIdata {
            return APIdata(parcel)
        }

        override fun newArray(size: Int): Array<APIdata?> {
            return arrayOfNulls(size)
        }
    }
}
