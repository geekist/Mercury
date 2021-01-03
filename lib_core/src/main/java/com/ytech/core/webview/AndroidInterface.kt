package com.ytech.core.webview

import com.just.agentweb.AgentWeb


class AndroidInterface(val fragment: WebClientFragment, private val agentWeb: AgentWeb) {
    /*
    @JavascriptInterface
    fun pushWebController(text: String) {
        L.e("H5 打开新页面 text = $text")
        runOnUI {
            val data = JsonUtils.fromJson(text, PushWebController::class.java)
            if (data != null) {
                val url = "${Constants.BASE_H5_URL}/${data.url}"
                WebPages.go(
                    fragment, url, data.isShowLocalTitleBar(),
                    data.isShowShareButton(), supportEventBus = true
                )
            }
        }
    }

    @JavascriptInterface
    fun goBack(param: String) {
        L.e("返回 goBack params = $param")
        runOnUI { popFragment(fragment) }
    }

    private fun popFragment(fragment: BaseFragment) {
        when (fragment) {
            is SupportFragment -> fragment.pop()
            is SupportMvpFragment<*> -> fragment.pop()
        }
    }

    private fun runOnUI(block: () -> Unit) {
        when (isUIThread()) {
            true -> block()
            else -> YuYa.getHandler().post { block() }
        }
    }
    */
}