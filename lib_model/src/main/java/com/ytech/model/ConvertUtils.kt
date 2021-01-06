package com.ytech.model

import android.text.TextUtils

/**
 * Create by liwen on 2020-05-19
 */
object ConvertUtils {

    @JvmStatic
    fun convertName(feed: DatasBean): String {

        return if (TextUtils.isEmpty(feed.author)) {
            "推荐者：${feed.shareUser}"
        } else {
            "作者：${feed.author}"
        }

    }

}