package com.ytech.core.arouter

import android.content.Context
import androidx.lifecycle.LiveData
import com.alibaba.android.arouter.facade.template.IProvider
import com.ytech.model.User

interface LoginService : IProvider {

    fun isLogin(): Boolean

    fun getUserInfo(): com.ytech.model.User?

    fun removeUserInfo()

    fun start(context: Context): LiveData<com.ytech.model.User>

    fun getLiveData(): LiveData<com.ytech.model.User>

}