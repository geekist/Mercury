package com.ytech.account

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tencent.mmkv.MMKV
import com.ytech.core.model.User

object UserManager {

    private const val USER_DATA: String = "user_data"
    private var mmkv: MMKV = MMKV.defaultMMKV()

    private val liveData = MutableLiveData<User>()

    fun getLoginLiveData(): MutableLiveData<User> {
        return liveData
    }

    fun getUser(): User? {
        return mmkv.decodeParcelable(USER_DATA, User::class.java)
    }

    fun saveUser(user: User?) {
        mmkv.encode(USER_DATA, user)
        if (liveData.hasObservers()) {
            liveData.postValue(user)
        }
    }

    fun isLogin(): Boolean {
        return getUser() != null
    }

    fun removeUser() {
        mmkv.encode(USER_DATA, "")
    }

    fun start(context: Context): LiveData<User> {
       // context.startActivity(Intent(context, LoginActivity::class.java))
        return liveData
    }


}