package com.ytech.core.arouter.provider

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

interface IWebViewProvider : IProvider {

    fun start(context: Context, title: String, url: String)
}