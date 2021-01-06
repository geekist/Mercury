package com.win.ft_login.service

import android.content.Context
import androidx.lifecycle.LiveData
import com.ytech.account.UserManager
import com.ytech.core.arouter.LoginService
import com.ytech.model.User

//@Route(path = ConstantsPath.LOGIN_SERVICE_PATH)
class LoginServiceImpl : LoginService {

    override fun isLogin(): Boolean {
        return UserManager.isLogin()
    }

    override fun getUserInfo(): com.ytech.model.User? {
        return UserManager.getUser()
    }

    override fun removeUserInfo() {
        UserManager.removeUser()
    }

    override fun start(context: Context): LiveData<com.ytech.model.User> {
        return UserManager.start(context)
    }

    override fun getLiveData(): LiveData<com.ytech.model.User> {
        return UserManager.getLoginLiveData()
    }

    override fun init(context: Context?) {

    }
}