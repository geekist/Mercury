package com.ytech.core.preference

import android.content.Context
import android.content.SharedPreferences
import com.ytech.common.preference.boolean
import com.ytech.common.preference.initKey
import com.ytech.common.preference.string
import com.ytech.core.constant.AppConstant
import com.ytech.core.GlobalConfig


class PrefsHelper private constructor(context: Context) {
    private val mPrefs: SharedPreferences =
        context.getSharedPreferences(AppConstant.APP_PREFS_KEY, Context.MODE_PRIVATE)

    companion object {
        private var instance: PrefsHelper? = null

        fun get(): PrefsHelper {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = PrefsHelper(GlobalConfig.getApplicationContext())
                    }
                }
            }
            return instance!!
        }
    }

    init {
        mPrefs.initKey(AppConstant.APP_ENCRYPT_KEY)
    }

    /**
     * 用户信息(加密)
     */
    var userInfo by mPrefs.string(key = "userInfo", isEncrypt = true)

    /**
     * 回话信息(接口调用需要传递sessionId)
     */
    var tokenInfo by mPrefs.string(key = "tokenInfo")

    /**
     * 图片选择数据缓存
     * 存前50张图片数据(实体类为FileBean)
     */
    var photoPickerData by mPrefs.string(key = "photoPickerData")

    /**
     * 视频选择数据缓存
     * 存前50张图片数据(实体类为FileBean)
     */
    var videoPickerData by mPrefs.string(key = "videoPickerData")

    /**
     * 是否显示服务协议
     */
    var showServiceAgreement by mPrefs.boolean(key = "showServiceAgreement", defValue = true)
}