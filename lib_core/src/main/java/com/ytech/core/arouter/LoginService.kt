package com.ytech.core.arouter

import android.content.Context
import androidx.lifecycle.LiveData
import com.alibaba.android.arouter.facade.template.IProvider
import com.ytech.core.model.User

interface LoginService : IProvider {

    fun isLogin(): Boolean

    fun getUserInfo(): User?

    fun removeUserInfo()

    fun start(context: Context): LiveData<User>

    fun getLiveData(): LiveData<User>

}