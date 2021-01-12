package com.ytech.model

import android.os.Parcel
import android.os.Parcelable


data class User(
    val id: Int = 0,
    val username: String,
    val icon: String = "",
    val email: String = "",
    val password: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        username = parcel.readString()!!,
        icon = parcel.readString()!!,
        email = parcel.readString()!!,
        password = parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(username)
        parcel.writeString(icon)
        parcel.writeString(email)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}