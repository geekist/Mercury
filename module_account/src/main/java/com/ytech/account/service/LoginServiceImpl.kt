package com.win.ft_login.service

import android.content.Context
import androidx.lifecycle.LiveData
import com.alibaba.android.arouter.facade.annotation.Route
import com.ytech.account.UserManager
import com.ytech.core.arouter.LoginService
import com.ytech.core.model.User

//@Route(path = ConstantsPath.LOGIN_SERVICE_PATH)
class LoginServiceImpl : LoginService {

    override fun isLogin(): Boolean {
        return UserManager.isLogin()
    }

    override fun getUserInfo(): User? {
        return UserManager.getUser()
    }

    override fun removeUserInfo() {
        UserManager.removeUser()
    }

    override fun start(context: Context): LiveData<User> {
        return UserManager.start(context)
    }

    override fun getLiveData(): LiveData<User> {
        return UserManager.getLoginLiveData()
    }

    override fun init(context: Context?) {

    }
}