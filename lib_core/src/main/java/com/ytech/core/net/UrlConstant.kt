package com.ytech.core.net

import com.ytech.core.BuildConfig

object UrlConstant {

    val BASE_URL: String
        get() = if (BuildConfig.DEBUG)
            BASE_URL_DEBUG
        else
            BASE_URL_RELEASE

    //Account
    val MESSAGE_BASE_URL: String
        get() = if (BuildConfig.DEBUG)
            MESSAGE_BASE_URL_RELEASE
        else
            MESSAGE_BASE_URL_DEBUG

    val BASE_H5_URL: String
        get() = if (BuildConfig.DEBUG)
            BASE_H5_URL_DEBUG
        else
            BASE_H5_URL_RELEASE

    // 发行版url
    private const val BASE_URL_RELEASE = "https://www.wanandroid.com"

    // 测试版url
    private const val BASE_URL_DEBUG = "https://www.wanandroid.com"

    const val BASE_URL_TEST = "https://www.wanandroid.com/user/login"


    const val MESSAGE_BASE_URL_RELEASE = "http://message.iyuya.com/api"
    const val MESSAGE_BASE_URL_DEBUG = "http://message.iyuya.com/api"

    // 发行版h5 url
    const val BASE_H5_URL_RELEASE = "http://html5.qilaimi.com"

    // 测试版h5 url
    const val BASE_H5_URL_DEBUG = "http://html5.qilaimi.com"

    const val BASE_MEDIA_URL = "http://pic.qilaimi.com/"
}