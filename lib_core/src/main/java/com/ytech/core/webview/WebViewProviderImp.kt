package com.ytech.core.webview

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.template.IProvider
import com.ytech.core.arouter.ARouterConstant

@Route(path = ARouterConstant.WebView.WEB_VIEW_PROVIDER_PATH)
class WebViewProviderImp : IProvider {

    fun start(context: Context, title: String, url: String) {
        WebViewActivity.start(context, title, url)
    }

    override fun init(context: Context?) {

    }

}