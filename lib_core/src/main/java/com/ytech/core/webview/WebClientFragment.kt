package com.ytech.core.webview

import android.os.Bundle
import android.webkit.ConsoleMessage
import android.webkit.WebView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bigkoo.convenientbanner.utils.ScreenUtil
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.ytech.common.common.getStatusBarHeight
import com.ytech.common.log.L
import com.ytech.common.text.isNotEmptyStr
import com.ytech.core.arouter.ARouterConstant
import com.ytech.core.constant.AppConstant
import com.ytech.ui.base.SupportFragment

@Route(path = ARouterConstant.LibCore.FRAGMENT_WEB_CLIENT)
open class WebClientFragment : SupportFragment() {

    companion object {
        // android注入到js的对象名称
        const val ANDROID_JS_OBJECT = "Android"
    }

    private var mUrl = ""

    /**
     * 是否显示TitleBar
     */
    private val mShowLocalTitleBar by lazy {
        arguments?.getBoolean(AppConstant.EXTRA_WEB_SHOW_LOCAL_TITLE_BAR, true) ?: true
    }

    /**
     * 是否显示返回按钮
     */
    private val mShowBackIcon by lazy {
        arguments?.getBoolean(AppConstant.EXTRA_WEB_SHOW_BACK_ICON, true) ?: true
    }

    /**
     * Web页面是否支持EventBus
     */
    private val mSupportEventBus by lazy {
        arguments?.getBoolean(AppConstant.EXTRA_WEB_SUPPORT_EVENT_BUS, false) ?: false
    }

    /**
     * WebView Fragment是否支持侧滑返回
     */
    private val mSupportSwipeBack by lazy {
        arguments?.getBoolean(AppConstant.EXTRA_SUPPORT_SWIPE_BACK, true) ?: true
    }

    protected open var mAgentWeb: AgentWeb? = null

    /*
    override fun initRootContainer() = R.layout.web_fragment_youka_web

    override fun initView(savedInstanceState: Bundle?, rootView: View) {
        if (!mShowLocalTitleBar) mTitleBar.hide()
        if (mShowBackIcon) mTitleBar.setNavigationIcon(R.drawable.ic_title_back_black)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUrl = arguments?.getString(AppConstant.EXTRA_WEB_URL, "") ?: ""
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
      //  initAgentWeb()
    }

    /*
    protected open fun initAgentWeb() {
        L.e("initAgentWeb")
        mAgentWeb = AgentWeb.with(this@WebClientFragment)
            // 传入AgentWeb的父控件
            .setAgentWebParent(
                mContentLayout,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            )
            // 设置进度条颜色与高度 单位为dp。
            .useDefaultIndicator(context!!.loadColor(R.color.cmn_ui_primary_color), 3)
            // 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
            .setWebViewClient(mWebViewClient)
            .setWebChromeClient(mWebChromeClient)
            // 严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            // 参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新，
            .setMainFrameErrorView(R.layout.web_layout_error, R.id.mBtnReload)
            .createAgentWeb()
            .ready()
            .go(mUrl)
        mAgentWeb?.agentWebSettings?.webSettings?.let { webSettings ->
            // 这里需要设置为true，才能让Webivew支持<meta>标签的viewport属性
            webSettings.useWideViewPort = true
            webSettings.userAgentString =
                webSettings.userAgentString + " xmandroidapp##v${context!!.getAppVersionName()}"
        }
        mAgentWeb?.let {
            // 注入Android对象
            it.jsInterfaceHolder.addJavaObject(
                ANDROID_JS_OBJECT,
                AndroidInterface(this@WebClientFragment, it)
            )
        }
        L.e("加载的url : $mUrl")
    }*/

    private val mWebViewClient by lazy {
        object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                view?.let {
                    it.evaluateJavascript(
                        "javascript:getAndroidStatusBarHeight(${context!!.getStatusBarHeight()},${ScreenUtil.getScreenWidth(
                            context!!
                        )})"
                    ) { }
                }
            }
        }
    }

    private val mWebChromeClient by lazy {
        object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String) {
                if (title.isNotEmptyStr()) {
                    val newTitle = if (title.length > 15) "${title.substring(0, 15)}..." else title
                  //  mTitleBar?.setTitle(newTitle)
                }
            }

            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                consoleMessage ?: return super.onConsoleMessage(consoleMessage)
                L.e("onConsoleMessage = ${consoleMessage.message()}")
                return super.onConsoleMessage(consoleMessage)
            }
        }
    }

    /*
    override fun refresh(needScrollToTop: Boolean) {
        val urlLoader = mAgentWeb?.urlLoader ?: return
        L.e("重新加载 url = $mUrl")
        urlLoader.stopLoading()
        urlLoader.reload()
    }*/

    /*override fun receiveLoginSuccessEvent() {
        AccountManager.getInstance().getToken()?.let { token ->
            L.e("好好养WebFragment old url = $mUrl")
            mUrl = WebPages.appendParams(mUrl, "token" to token)
            L.e("好好养WebFragment 重新加载 url = $mUrl")
            mAgentWeb?.urlLoader?.loadUrl(mUrl)
        }
    }*/

    override fun onResume() {
        mAgentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onPause() {
        mAgentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mAgentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }
/*
    override fun getTitleBar(): TitleBar? = mTitleBar

    override fun supportEventBus() = mSupportEventBus

    override fun supportSwipeBack() = mSupportSwipeBack
*/
    override fun onBackPressedSupport(): Boolean {
        if (mAgentWeb?.back() == true) {
            return true
        }
        return super.onBackPressedSupport()
    }
}