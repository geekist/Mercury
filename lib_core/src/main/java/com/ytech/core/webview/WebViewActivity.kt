package com.ytech.core.webview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.webkit.*
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.ytech.core.R
import com.ytech.core.arouter.ARouterConstant
import com.ytech.ui.base.SupportActivity
import kotlinx.android.synthetic.main.activity_web_client.*

@Route(path = ARouterConstant.LibCore.ACTIVITY_WEB_CLIENT)
class WebViewActivity : SupportActivity() {

    @Autowired
    lateinit var title: String

    @Autowired
    lateinit var url: String

    companion object {
        fun start(context: Context, title: String, url: String) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("title", title)
            intent.putExtra("url", url)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ARouter.getInstance().inject(this)

        StatusBarKt.fitSystemBar(this)
        setContentView(R.layout.activity_web_client)

        initView()
//        mCollect.setOnClickListener {
////            mCollect.imageTintList =
////                ColorStateList.valueOf(resources.getColor((R.color.imageView_tint)))
//
//        }
    }

    fun initView() {
        initActionBar()
        initWebView()
    }

    private fun initActionBar() {
        mTvTitle.text = title
        mIvBack.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("JavascriptInterface")
    private fun initWebView() {
        val settings = mWebView.settings
        settings.allowContentAccess = true
        settings.domStorageEnabled = true
        settings.allowFileAccess = true
        settings.javaScriptEnabled = true

        mWebView.addJavascriptInterface(this, "wan")

        mWebView.webChromeClient = object : WebChromeClient() {

        }

        mWebView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.proceed()
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                Log.d("","")
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        mWebView.loadUrl(url)
    }
}