package com.ytech.account.model

import androidx.databinding.BaseObservable
import com.ytech.account.BR

class LoginLayoutBean : BaseObservable() {

    var tips: String = "密码登录"
        get() {
            field = if (isLogin) {
                "密码登录"
            } else {
                "账号注册"
            }
            return field
        }

    var btnText: String = "登录"
        get() {
            field = if (isLogin) {
                "登录"
            } else {
                "注册"
            }
            return field
        }

    var featureName: String = "注册"
        get() {
            field = if (isLogin) {
                "注册"
            } else {
                "登录"
            }
            return field
        }

    var isLogin: Boolean = true
        set(value) {
            if (value == field) {
                return
            }
            field = value
            notifyPropertyChanged(BR._all)
        }
}