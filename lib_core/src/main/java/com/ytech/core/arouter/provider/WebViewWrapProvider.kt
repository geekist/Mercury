package com.ytech.core.arouter.provider

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.ytech.core.arouter.ARouterConstant

class WebViewWrapProvider private constructor() {

    @Autowired(name = ARouterConstant.WebView.WEB_VIEW_PROVIDER_PATH)
    lateinit var provider: IWebViewProvider

    init{
        ARouter.getInstance().inject(this)
    }

    companion object {
        val instance = Singleton.holder

        object Singleton {
            val holder = WebViewWrapProvider()
        }
    }

    fun start(context: Context, title: String, url: String) {
        provider.start(context,title,url)
    }
}