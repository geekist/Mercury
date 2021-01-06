package com.ytech.account

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tencent.mmkv.MMKV
import com.ytech.model.User

object UserManager {

    private const val USER_DATA: String = "user_data"
    private var mmkv: MMKV = MMKV.defaultMMKV()

    private val liveData = MutableLiveData<com.ytech.model.User>()

    fun getLoginLiveData(): MutableLiveData<com.ytech.model.User> {
        return liveData
    }

    fun getUser(): com.ytech.model.User? {
        return mmkv.decodeParcelable(USER_DATA, com.ytech.model.User::class.java)
    }

    fun saveUser(user: com.ytech.model.User?) {
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

    fun start(context: Context): LiveData<com.ytech.model.User> {
       // context.startActivity(Intent(context, LoginActivity::class.java))
        return liveData
    }


}